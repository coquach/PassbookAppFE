package com.se104.passbookapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.viewModels


import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BasePassbookAppActivity : ComponentActivity(){

    val viewModel by viewModels<MainViewModel>()


}