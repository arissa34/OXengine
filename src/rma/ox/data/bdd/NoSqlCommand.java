package rma.ox.data.bdd;

import rma.ox.engine.core.utils.IdCounter;

public enum NoSqlCommand {

    ERROR(IdCounter.getNextNumber()),
    INSERT(IdCounter.getNextNumber()),
    INSERT_OR_UPDATE(IdCounter.getNextNumber()),
    UPDATE(IdCounter.getNextNumber()),
    FIND_BY_ID(IdCounter.getNextNumber()),
    FIND_FIRST(IdCounter.getNextNumber()),
    FIND_ALL(IdCounter.getNextNumber()),
    FIND_BY_FILTER(IdCounter.getNextNumber()),
    REMOVE_BY_ID(IdCounter.getNextNumber()),
    REMOVE(IdCounter.getNextNumber());

    public int id;
    NoSqlCommand(int id){
        this.id = id;
    }
}
