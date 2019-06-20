import android.app.Application
import crystalapps.net.mint.tools.service.pool.ServiceLocator

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        ServiceLocator.loadContextService(this)
    }
}