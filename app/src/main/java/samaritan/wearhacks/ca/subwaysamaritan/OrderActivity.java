package samaritan.wearhacks.ca.subwaysamaritan;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nuance.nmdp.speechkit.Audio;
import com.nuance.nmdp.speechkit.AudioPlayer;
import com.nuance.nmdp.speechkit.DetectionType;
import com.nuance.nmdp.speechkit.Interpretation;
import com.nuance.nmdp.speechkit.Language;
import com.nuance.nmdp.speechkit.Recognition;
import com.nuance.nmdp.speechkit.Session;
import com.nuance.nmdp.speechkit.Transaction;
import com.nuance.nmdp.speechkit.TransactionException;

import java.util.ArrayList;

import Objects.Command;
import Objects.Info;
import database.DatabaseConnection;

/**
 * Created by HenryChiang on 15-10-03.
 */
public class OrderActivity extends AppCompatActivity implements View.OnClickListener,AudioPlayer.Listener{

    private Audio startEarcon;
    private Audio stopEarcon;
    private Audio errorEarcon;

    private DatabaseConnection dataConnection;


    private final String TAG="NLU";
    private final String TAG2="TTS";

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mOrderAdapter;
    private ArrayList<Message> messageArrayList;


    private Button commandBtn;

    private Session speechSession,speakSession;
    private Transaction recoTransaction;
    private State state = State.IDLE;

    public  Transaction ttsTransaction;

    public Command command;

    private SubwayMaker subwayMaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        messageArrayList = new ArrayList<>();
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        commandBtn = (Button)findViewById(R.id.orderBtn);
        commandBtn.setOnClickListener(this);
        commandBtn.setTypeface(FontManager.getTypeface(getApplicationContext()));
        mOrderAdapter = new RecyclerViewAdapter(getApplicationContext(),messageArrayList);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mOrderAdapter);


        //Create a session
        speechSession = Session.Factory.session(this, Config.SERVER_URI, Config.APP_KEY);
        speechSession.getAudioPlayer().setListener(this);
        speechSession.getAudioPlayer().play();
        loadEarcons();
        setState(State.IDLE);

        //Create another session
        speakSession = Session.Factory.session(this, Config.SERVER_URI, Config.APP_KEY);
        speakSession.getAudioPlayer().setListener(this);


        //SubwayMaker
        dataConnection = new DatabaseConnection(this);
        try{
            dataConnection.open();
        }
        catch (Exception e)
        { }

        subwayMaker= new SubwayMaker(dataConnection.getAllFood());




    }

    @Override
    public void onClick(View v) {
            toggleReco();
    }

    /* Reco transactions */

    private void toggleReco() {
        switch (state) {
            case IDLE:
                speechSession.getAudioPlayer().enqueue(startEarcon);
                break;
            case LISTENING:
                stopRecording();
                break;
            case PROCESSING:
                cancel();
                break;
        }
    }

    @Override
    public void onBeginPlaying(AudioPlayer audioPlayer, Audio audio) {
    }

    @Override
    public void onFinishedPlaying(AudioPlayer audioPlayer, Audio audio) {
        if(getState()==State.DONE) {
            setState(State.IDLE);
        }else{
            recognize();
        }

    }


    /**
     * Start listening to the user and streaming their voice to the server.
     */
    private void recognize() {
        Transaction.Options options = new Transaction.Options();
        options.setDetection(resourceIDToDetectionType());
        options.setLanguage(new Language("eng-USA"));

        JSONObject appServerData = new JSONObject();
        recoTransaction = speechSession.recognizeWithService(Config.BOLT_MODEL_TAG, appServerData, options, recoListener);
    }

    private Transaction.Listener recoListener = new Transaction.Listener() {
        @Override
        public void onStartedRecording(Transaction transaction) {
            Log.d(TAG,"\nonStartedRecording");

            //We have started recording the users voice.
            //We should update our state and start polling their volume.
            setState(State.LISTENING);
        }

        @Override
        public void onFinishedRecording(Transaction transaction) {
            Log.d(TAG,"\nonFinishedRecording");
            setState(State.PROCESSING);
        }

        @Override
        public void onServiceResponse(Transaction transaction, org.json.JSONObject response) {
            try {
                // 2 spaces for tabulations.
                Log.d(TAG, "\nonServiceResponse"+ response.toString(2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setState(State.IDLE);
        }

        @Override
        public void onRecognition(Transaction transaction, Recognition recognition) {
            //logs.append("\nonRecognition: " + recognition.getText());
            Log.d(TAG, "\nonRecognition: " + recognition.getText());
            //We have received a transcription of the users voice from the server.
            setState(State.IDLE);
        }

        @Override
        public void onInterpretation(Transaction transaction, Interpretation interpretation) {
            try {
                //logs.append("\nonInterpretation: " + interpretation.getResult().toString(2));
                Log.d(TAG, "\nonInterpretation: " + interpretation.getResult().toString(2));
                JSONObject jsonInterpretations = interpretation.getResult().getJSONArray("interpretations").getJSONObject(0);
                Log.d(TAG, "What I said:  " + jsonInterpretations.getString("literal"));

                //get intent
                JSONObject json_intent = jsonInterpretations.getJSONObject("action").getJSONObject("intent");
                Log.d(TAG, "The intent: " + json_intent.getString("value"));

                ArrayList<Info> infoArrayList = new ArrayList<>();


                //get OBJECTs of concept
                //if there is any concept
                if(jsonInterpretations.has("concepts")){
                    JSONArray tempJsonArray = jsonInterpretations.getJSONObject("concepts").names();
                    for(int i = 0;i<tempJsonArray.length();i++){
                        Log.d(TAG, "What is the concept: " + String.valueOf(tempJsonArray.getString(i)));
                        Log.d(TAG, "literal of concept: " + String.valueOf(jsonInterpretations.getJSONObject("concepts").getJSONArray(tempJsonArray.getString(i)).getJSONObject(0).getString("literal")));

                        Info tempInfo = new Info(tempJsonArray.getString(i),jsonInterpretations.getJSONObject("concepts").getJSONArray(tempJsonArray.getString(i)).getJSONObject(0).getString("literal").toLowerCase());
                        infoArrayList.add(tempInfo);

                    }


                    //if no concept
                }else{

                    Info infoToPassed;
                    if(json_intent.getString("value").equals("ORDER_INTENT") || json_intent.getString("value").equals("NO_MATCH")) {
                       infoToPassed = new Info("OBJECT", jsonInterpretations.getString("literal"));
                    }else {
                        infoToPassed = new Info("", "");

                    }

                    infoArrayList.add(infoToPassed);

                }

                //display what i said
                if(jsonInterpretations.getString("literal")!=null){
                    Message msg = new Message(jsonInterpretations.getString("literal"),2);
                    messageArrayList.add(msg);
                    mOrderAdapter.swap(messageArrayList);

                }


                if(infoArrayList.size()!= 0) {
                    if(json_intent.getString("value").equals("NO_MATCH")){
                        command = new Command("ORDER_INTENT", infoArrayList);
                    }else{
                        command = new Command(json_intent.getString("value"), infoArrayList);
                    }
                    String tempStr =subwayMaker.responser(command);
                    toggleTTS(tempStr);
                    Message serverMsg = new Message(tempStr,1);
                    messageArrayList.add(serverMsg);
                    mOrderAdapter.swap(messageArrayList);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            setState(State.SPEAKING);
        }

        @Override
        public void onSuccess(Transaction transaction, String s) {
            Log.d(TAG,"\nonSuccess");
        }

        @Override
        public void onError(Transaction transaction, String s, TransactionException e) {
            Log.d(TAG, "\nonError: " + e.getMessage() + ". " + s);
            setState(State.IDLE);
        }
    };

    /**
     * Stop recording the user
     */
    private void stopRecording() {
        recoTransaction.stopRecording();
    }

    /**
     * Cancel the Reco transaction.
     * This will only cancel if we have not received a response from the server yet.
     */
    private void cancel() {
        recoTransaction.cancel();
    }

    /* State Logic: IDLE -> LISTENING -> PROCESSING -> repeat */

    private enum State {
        IDLE,
        LISTENING,
        PROCESSING,
        SPEAKING,
        DONE
    }


    /**
     * Set the state and update the button text.
     */
    private void setState(State newState) {
        state = newState;
        switch (newState) {
            case IDLE:
                commandBtn.setText(getResources().getString(R.string.commandBtn_idle));
                break;
            case LISTENING:
                commandBtn.setText(getResources().getString(R.string.commandBtn_listen));
                break;
            case PROCESSING:
                commandBtn.setText(getResources().getString(R.string.commandBtn_process));
                break;
        }
    }

    private State getState(){
        return state;
    }


    /* Earcons */

    private void loadEarcons() {
        //Load all of the earcons from disk
        startEarcon = new Audio(this, R.raw.sk_start, null);
        startEarcon.load();

        stopEarcon = new Audio(this, R.raw.sk_stop, null);
        stopEarcon.load();

        errorEarcon = new Audio(this, R.raw.sk_error, null);
        errorEarcon.load();
    }


    /* Helpers */

    private DetectionType resourceIDToDetectionType() {
        return DetectionType.Long;
    }


    private void toggleTTS(String textToSpeech) {
        //If we are not loading TTS from the server, then we should do so.
        if(ttsTransaction == null) {
            startTTS(textToSpeech);
        }
        //Otherwise lets attempt to cancel that transaction
        else {
            stopTTS();
        }
    }

    public void startTTS(String textToSpeech) {



        Transaction.Options options = new Transaction.Options();
        options.setLanguage(new Language("eng-USA"));

        speakSession.getAudioPlayer().play();

        ttsTransaction = speakSession.speakString(textToSpeech, options, new Transaction.Listener() {
            @Override
            public void onAudio(Transaction transaction, Audio audio) {
                Log.d(TAG2, "\nonAudio");
                ttsTransaction = null;
                speakSession.getAudioPlayer().enqueue(audio);
            }

            @Override
            public void onSuccess(Transaction transaction, String s) {
                Log.d(TAG2, "\nonSuccess");
                setState(State.DONE);

            }

            @Override
            public void onError(Transaction transaction, String s, TransactionException e) {
                Log.d(TAG2, "\nonError: " + e.getMessage() + ". " + s);
                ttsTransaction = null;
                setState(State.DONE);
            }
        });


    }

    private void stopTTS() {
        ttsTransaction.cancel();
    }
}
