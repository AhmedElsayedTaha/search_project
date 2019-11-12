package apit.net.sa.searchcontactapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import apit.net.sa.searchcontactapp.views.LocalSearchActivity;
import apit.net.sa.searchcontactapp.views.RemoteSearchActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
    }


    @OnClick(R.id.btn_local_search)
    public void localBtnClick(){
        startActivity(new Intent(this, LocalSearchActivity.class));
    }

    @OnClick(R.id.btn_remote_search)
    public void remoteBtnClick(){
        startActivity(new Intent(this, RemoteSearchActivity.class));
    }


}
