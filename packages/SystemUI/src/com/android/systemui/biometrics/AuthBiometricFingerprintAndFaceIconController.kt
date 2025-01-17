/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.biometrics

import android.annotation.RawRes
import android.content.Context
import com.airbnb.lottie.LottieAnimationView
import com.android.systemui.R
import com.android.systemui.biometrics.AuthBiometricView.BiometricState
import com.android.systemui.biometrics.AuthBiometricView.STATE_AUTHENTICATED
import com.android.systemui.biometrics.AuthBiometricView.STATE_ERROR
import com.android.systemui.biometrics.AuthBiometricView.STATE_HELP
import com.android.systemui.biometrics.AuthBiometricView.STATE_PENDING_CONFIRMATION

/** Face/Fingerprint combined icon animator for BiometricPrompt. */
class AuthBiometricFingerprintAndFaceIconController(
        context: Context,
        iconView: LottieAnimationView,
        iconViewOverlay: LottieAnimationView
) : AuthBiometricFingerprintIconController(context, iconView, iconViewOverlay) {

    override val actsAsConfirmButton: Boolean = true

    override fun shouldAnimateIconViewForTransition(
            @BiometricState oldState: Int,
            @BiometricState newState: Int
    ): Boolean = when (newState) {
        STATE_PENDING_CONFIRMATION -> true
        STATE_AUTHENTICATED -> false
        else -> super.shouldAnimateIconViewForTransition(oldState, newState)
    }

    @RawRes
    override fun getAnimationForTransition(
            @BiometricState oldState: Int,
            @BiometricState newState: Int
    ): Int? = when (newState) {
        STATE_AUTHENTICATED -> {
           if (oldState == STATE_PENDING_CONFIRMATION) {
               R.raw.fingerprint_dialogue_unlocked_to_checkmark_success_lottie
           } else {
               super.getAnimationForTransition(oldState, newState)
           }
        }
        STATE_PENDING_CONFIRMATION -> {
            if (oldState == STATE_ERROR || oldState == STATE_HELP) {
                R.raw.fingerprint_dialogue_error_to_unlock_lottie
            } else {
                R.raw.fingerprint_dialogue_fingerprint_to_unlock_lottie
            }
        }
        STATE_AUTHENTICATED -> null
        else -> super.getAnimationForTransition(oldState, newState)
    }
}
