package com.example.contactapp.utils

object MyEventBus {
    var reloadEvent : (() -> Unit)?= null
    var checkNetwork : (() -> Unit)?= null
}