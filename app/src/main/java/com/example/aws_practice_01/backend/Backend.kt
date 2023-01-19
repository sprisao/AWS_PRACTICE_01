package com.example.aws_practice_01.backend

import android.app.Activity
import android.content.Context
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.AuthChannelEventName
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.auth.result.AuthSessionResult
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.InitializationStatus
import com.amplifyframework.hub.HubChannel
import com.amplifyframework.hub.HubEvent
import com.example.aws_practice_01.data.model.UserData
import timber.log.Timber

object Backend {
    private const val TAG = "Backend"

    fun initialize(applicationContext: Context): Backend {
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(applicationContext)
            Timber.d("Initialized Amplify")
        } catch (error: AmplifyException) {
            Timber.e("Could not initialize Amplify $error")
        }

        Timber.i("registering hub event")

        Amplify.Hub.subscribe(HubChannel.AUTH) { hubEvent: HubEvent<*> ->
            when (hubEvent.name) {
                InitializationStatus.SUCCEEDED.toString() -> {
                    Timber.i("Initialized Auth plugin")
                }
                InitializationStatus.FAILED.toString() -> {
                    Timber.e("Failed to initialize Auth plugin")
                }
                else -> {
                    when (AuthChannelEventName.valueOf(hubEvent.name)) {
                        AuthChannelEventName.SIGNED_IN -> {
                            updateUserData(true)
                            Timber.i("User signed in")
                        }
                        AuthChannelEventName.SIGNED_OUT -> {
                            updateUserData(false)
                            Timber.i("User signed out")
                        }
                        else -> {
                            Timber.i("Unhandled Auth Event: ${hubEvent.name}")
                        }
                    }
                }

            }
        }
        Timber.i("Retrieving session status")

        // is user alerady authenticated (from a previous execution) ?
        Amplify.Auth.fetchAuthSession(
            { result ->
                Timber.i("Session = $result")
                val cognitoAuthSession =
                    result as com.amplifyframework.auth.cognito.AWSCognitoAuthSession

                // update UI
                this.updateUserData(cognitoAuthSession.isSignedIn)
                when (cognitoAuthSession.identityId.type) {
                    AuthSessionResult.Type.SUCCESS -> {
                        Timber.i("IdentityId = ${cognitoAuthSession.identityId.value}")
                    }
                    AuthSessionResult.Type.FAILURE -> {
                        Timber.e("IdentityId = ${cognitoAuthSession.identityId.error}")
                    }
                }
            },
            { error -> Timber.e("Failed to fetch session = $error") }
        )

        return this
    }


    private fun updateUserData(withSignedStatus: Boolean) {
        UserData.setSigendIn(withSignedStatus)
    }

    fun signOut() {
        Timber.i("initiate sign out Sequence")

        Amplify.Auth.signOut(
            { Timber.i("sign out success") },
            { error: AuthException -> Timber.e("sign out failed $error") }
        )
    }

    fun signIn(callingActivity: Activity) {
        Timber.i("initiate sign in Sequence")

        Amplify.Auth.signInWithWebUI(
            callingActivity,
            { result -> Timber.i("sign in success $result") },
            { error: AuthException -> Timber.e("sign in failed $error") }
        )
    }


}
