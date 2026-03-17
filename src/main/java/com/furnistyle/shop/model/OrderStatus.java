package com.furnistyle.shop.model;

public enum OrderStatus {
    CREATED{
        @Override
        public boolean canChangeTo(OrderStatus next){
            return next == CONFIRMED || next == CANCELLED;
        }
    },
    CONFIRMED{
        @Override
        public boolean canChangeTo(OrderStatus next){
            return next == IN_PROGRESS || next == CANCELLED;
        }
    },
    IN_PROGRESS{
        @Override
        public boolean canChangeTo(OrderStatus next){
            return next == DONE || next == CANCELLED;
        }
    },
    DONE{
        @Override
        public boolean canChangeTo(OrderStatus next){
            return false;
        }
    },
    CANCELLED{
        @Override
        public boolean canChangeTo(OrderStatus next){
            return false;
        }
    };

    public abstract boolean canChangeTo(OrderStatus next);

    public boolean isFinished(){
        return this == DONE || this == CANCELLED;
    }
}
