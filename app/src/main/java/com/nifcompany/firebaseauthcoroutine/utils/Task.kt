package com.nifcompany.firebaseauthcoroutine.utils

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun <T> Task<T>.await(): Result<T>{
    if (isComplete)
    {
        val e = exception
        return if (e==null){
            if (isCanceled){
                Result.Canceled(CancellationException("Task $this was cancelled normally."))
            }
            else{
                @Suppress("UNCHECKED_CAST")
                Result.Success(result as T)
            }
        }
        else{
            Result.Error(e)
        }
    }

    return suspendCancellableCoroutine { cont ->
        addOnCompleteListener {
            val e = exception
            if (e == null)
            {
                @Suppress("UNCHECKES_CAST")
                if (isCanceled){
                    cont.cancel()
                }
                else{
                    cont.resume(Result.Success(result as T))
                }
            }
            else{
                cont.resumeWithException(e)
            }
        }
    }
}