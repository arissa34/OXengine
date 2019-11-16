package rma.ox.data.bdd.base;


public interface DBQueryScheduler {

    void execute(Runnable runnable);
}
