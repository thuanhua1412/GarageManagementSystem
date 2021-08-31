package com.example.dataclasses

abstract class CarComponent{
    abstract val name : String
    abstract val model : String
    abstract var state: String
}

class GearBox(override val name: String = "GearBox", override val model: String = "Toshiba",override var state: String) : CarComponent()

class Wheel(override val name :String = "Wheel",  override val model: String ="Toshiba", override var state: String) : CarComponent()

class WindShield(override val name :String = "WindShield",  override val model: String ="HuynDai", override var state: String) : CarComponent()

class Engine(override val name :String = "Engine",  override val model: String ="HuynDai", override var state: String) : CarComponent()