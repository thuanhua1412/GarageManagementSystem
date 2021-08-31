package com.example.dataclasses


interface Subject {
    abstract var observers : ArrayList<Observer>
    abstract val data : Any
    fun registerObserver(observer: Observer){
        observers.add(observer)
    } // Add an Observer into the list (So they are notified about data changes)

    fun unsubscribeObserver(observer : Observer){
        observers.remove(observer)
    } // Remove the observer from the list
    fun notifyObservers(){
        for (observer : Observer in observers){
            observer.update(data)
        }
    }
}