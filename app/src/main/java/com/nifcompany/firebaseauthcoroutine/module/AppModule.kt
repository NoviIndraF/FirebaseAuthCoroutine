package com.nifcompany.firebaseauthcoroutine.module

import com.nifcompany.firebaseauthcoroutine.repository.FirebaseViewModel
import com.nifcompany.firebaseauthcoroutine.repository.implementation.UserRepositoryImpl
import org.koin.dsl.module

val firebaseViewModule = module{
    single { FirebaseViewModel(UserRepositoryImpl()) }
}