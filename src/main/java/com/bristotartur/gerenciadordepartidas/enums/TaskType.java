package com.bristotartur.gerenciadordepartidas.enums;

import com.bristotartur.gerenciadordepartidas.domain.events.TaskEvent;

public enum TaskType implements EventType<TaskEvent> {
    NORMAL,
    ACCOMPLISHED,
    CULTURAL
    
}
