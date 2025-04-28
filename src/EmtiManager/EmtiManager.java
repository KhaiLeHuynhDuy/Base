/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EmtiManager;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import nro.jdbc.daos.PlayerDAO;
import nro.network.server.GirlkunSessionManager;
import nro.server.Client;
import nro.server.Maintenance;

/**
 *
 * @author Lucy An Trom
 */
public class EmtiManager {

    private static EmtiManager instance = null;

    private EmtiManager() {
        compositeDisposable = new CompositeDisposable();
    }

    // Static method
    // Static method to create instance of Singleton class
    public static synchronized EmtiManager getInstance() {
        if (instance == null) {
            instance = new EmtiManager();
        }
        return instance;
    }

    private CompositeDisposable compositeDisposable;

//    public void autoSave() {
//        System.out.println("[AutoSaveManager] start autosave");
//        Disposable subscribe = Observable.interval(60, 90, TimeUnit.SECONDS)
//                .observeOn(Schedulers.io())
//                .subscribe(i -> {
//                    this.handleAutoSave();
//                }, throwable -> {
//                    System.out.println("[AutoSaveManager] start autosave error: " + throwable.getLocalizedMessage());
//                });
//        compositeDisposable.add(subscribe);
//    }
    private ScheduledExecutorService scheduler;

    public void startAutoSave() {
        int plcount = Client.gI().getPlayers().size();
        int sscount = GirlkunSessionManager.gI().getSessions().size();
        if (sscount >= plcount + 10) {
            Maintenance.gI().start(60);
        }
        scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            try {
                handleAutoSave();
            } catch (Exception e) {
                System.out.println("[AutoSaveManager] start autosave error: " + e.getLocalizedMessage());
            }
        }, 60, 90, TimeUnit.SECONDS);
    }

    public void handleAutoSave() {
        System.out.println("[AutoSaveManager] start autosave sucessfully !!");
        Client.gI().getPlayers().forEach(player -> {
            PlayerDAO.updatePlayer(player);
        });
    }

    private void dispose() {
        compositeDisposable.dispose();
        compositeDisposable = null;
    }
}
