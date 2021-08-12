package io.github.diegoflassa.template_android_app

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import java.lang.ref.WeakReference

//@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        context = WeakReference<Context>(applicationContext)
        setupKoin()
    }

    private fun setupKoin() {
        val myModules: Module = module {
            viewModel { AllPatientsFragmentViewModel(get()) }
            viewModel { PatientDetailsFragmentViewModel(get()) }
            viewModel { SearchBarFragmentViewModel(get()) }
        }
        startKoin {
            // Fix bug of koin initialization
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(myModules)
        }
    }

    companion object {
        private val TAG = MyApplication::class.simpleName
        private lateinit var context: WeakReference<Context>
        fun getContext(): Context {
            return context.get()!!
        }
    }
}
