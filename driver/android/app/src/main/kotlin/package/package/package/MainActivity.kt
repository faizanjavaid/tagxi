package package name

import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import android.content.Intent

class MainActivity: FlutterFragmentActivity() {

    private val channel = "flutter.app/awake"

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
    MethodChannel(flutterEngine.dartExecutor.binaryMessenger, channel).setMethodCallHandler {
      call, result ->
      // This method is invoked on the main thread.
      // TODO
      if(call.method == "awakeapp"){
          awakeapp()
      }
    }
    }

    private fun awakeapp(){
        val bringToForegroundIntent = Intent(this,MainActivity::class.java);
startActivity(bringToForegroundIntent);
    }


}
