package com.udacity.project4.util

import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.udacity.project4.utils.wrapEspressoIdlingResource
import org.hamcrest.Matcher
import org.hamcrest.Matchers

fun clickAt(lat: Double, lng: Double): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return Matchers.instanceOf(MapView::class.java)
        }

        override fun getDescription(): String {
            return "click at ($lat, $lng)"
        }

        override fun perform(uiController: UiController?, view: View?) {
            wrapEspressoIdlingResource {
                val mapView = view as MapView
                mapView.getMapAsync { googleMap ->
                    val projection = googleMap.projection
                    val point = projection.toScreenLocation(LatLng(lat, lng))
                    val x = point.x.toFloat()
                    val y = point.y.toFloat()

                    val motionEventDown = MotionEvent.obtain(
                        SystemClock.uptimeMillis(),
                        SystemClock.uptimeMillis(),
                        MotionEvent.ACTION_DOWN,
                        x,
                        y,
                        0
                    )

                    val motionEventUp = MotionEvent.obtain(
                        SystemClock.uptimeMillis(),
                        SystemClock.uptimeMillis(),
                        MotionEvent.ACTION_UP,
                        x,
                        y,
                        0
                    )

                    mapView.dispatchTouchEvent(motionEventDown)
                    mapView.dispatchTouchEvent(motionEventUp)
                }
            }

        }
    }
}
