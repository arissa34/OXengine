package rma.ox.data.bdd.base;


/**
 * Interface for schedulers, see {@link UseCaseThreadPoolScheduler}.
 */
public interface DBQueryScheduler {

    void execute(Runnable runnable);
}
