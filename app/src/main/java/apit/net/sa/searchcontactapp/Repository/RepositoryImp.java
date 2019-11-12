package apit.net.sa.searchcontactapp.Repository;

import android.util.Log;
import android.widget.EditText;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.sql.Time;
import java.util.List;
import java.util.concurrent.TimeUnit;

import apit.net.sa.searchcontactapp.Models.UserModel;
import apit.net.sa.searchcontactapp.Network.APIClient;
import apit.net.sa.searchcontactapp.Network.APIService;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class RepositoryImp implements UserInterface.UserRepositoryInterface {
    private final PublishSubject<String> publishSubject = PublishSubject.create();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    public void getUsers(final OnFinishedListner onFinishedListner, String source, String query) {
        APIService apiService= APIClient.getInstanceRetrofit().create(APIService.class);
        compositeDisposable.add(apiService.getUserData(source,query)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableSingleObserver<List<UserModel>>() {
            @Override
            public void onSuccess(List<UserModel> userModels) {
                onFinishedListner.onFnished(userModels);
            }

            @Override
            public void onError(Throwable e) {
                onFinishedListner.onFailed(e);
            }
        }));
    }

    @Override
    public void getUsersRemotes(final OnFinishedListner onFinishedListner, final String source, final String query, EditText editText) {

        final APIService apiService = APIClient.getInstanceRetrofit().create(APIService.class);

        compositeDisposable.add(publishSubject.debounce(300,TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .switchMapSingle(new Function<String, Single<List<UserModel>>>() {
            @Override
            public Single<List<UserModel>> apply(String s) throws Exception {
                return apiService.getRemotFilterUser(null,query)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

            }
        })
        .subscribeWith(new DisposableObserver<List<UserModel>>() {
            @Override
            public void onNext(List<UserModel> userModels) {
                onFinishedListner.onFnishedRemote(userModels);
            }

            @Override
            public void onError(Throwable e) {
                onFinishedListner.onFailedRemote(e);
            }

            @Override
            public void onComplete() {

            }
        }));

        publishSubject.onNext(query);



       /* RxTextView.textChangeEvents(editText)
                .skipInitialValue()
                .debounce(900,TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TextViewTextChangeEvent>() {
                    @Override
                    public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                        apiService.getRemotFilterUser(null,textViewTextChangeEvent.text().toString())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(new DisposableSingleObserver<List<UserModel>>() {
                                    @Override
                                    public void onSuccess(List<UserModel> userModels) {
                                        onFinishedListner.onFnishedRemote(userModels);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        onFinishedListner.onFailedRemote(e);
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("errorr","error is "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });*/



        /*
         * PublishSubject – You can notice PublishSubject introduced in this activity. PublishSubject emits the events at the time of subscription.
         * In our case, calling publishSubject.onNext() invokes the emission of Observable again thus making newer network call.
         */
      /*  final PublishSubject<String> publishSubject = PublishSubject.create();
        final APIService apiService = APIClient.getInstanceRetrofit().create(APIService.class);
        compositeDisposable.add( publishSubject.debounce(8, TimeUnit.SECONDS)
        .distinctUntilChanged()
        .switchMapSingle(new Function<String, Single<List<UserModel>>>() {
            @Override
            public Single<List<UserModel>> apply(String s) throws Exception {
                return apiService.getRemotFilterUser(source,s)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());

            }
        })
        .subscribeWith(new DisposableObserver<List<UserModel>>() {
            @Override
            public void onNext(List<UserModel> userModels) {
                onFinishedListner.onFnishedRemote(userModels);
            }

            @Override
            public void onError(Throwable e) {
                onFinishedListner.onFailedRemote(e);
            }

            @Override
            public void onComplete() {

            }
        }));*/

        /*compositeDisposable.add(RxTextView.textChangeEvents(editText)
                .skipInitialValue()
                .debounce(300, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TextViewTextChangeEvent>() {
                    @Override
                    public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                        publishSubject.onNext(textViewTextChangeEvent.text().toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("error","error in rxText"+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                }));*/
    }
}
