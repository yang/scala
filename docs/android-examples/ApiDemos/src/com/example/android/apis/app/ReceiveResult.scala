/*
 * Copyright (C) 2007 The Android Open Source Project
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

package com.example.android.apis.app

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import java.util.Map

import com.example.android.apis.R

import _root_.android.app.Activity
import _root_.android.app.Activity._
import _root_.android.content.Intent
import _root_.android.text.Editable
import _root_.android.os.Bundle
import _root_.android.view.View
import _root_.android.view.View.OnClickListener
import _root_.android.widget.{Button, TextView}

/**
 * Shows how an activity can send data to its launching activity when done.y.
 * <p>This can be used, for example, to implement a dialog alowing the user to
pick an e-mail address or image -- the picking activity sends the selected
data back to the originating activity when done.</p>

<p>The example here is composed of two activities: ReceiveResult launches
the picking activity and receives its results; SendResult allows the user
to pick something and sends the selection back to its caller.  Implementing
this functionality involves the
{@link android.app.Activity#setResult setResult()} method for sending a
result and
{@link android.app.Activity#onActivityResult onActivityResult()} to
receive it.</p>

<h4>Demo</h4>
App/Activity/Receive Result
 
<h4>Source files</h4>
<table class="LinkTable">
        <tr>
            <td class="LinkColumn">src/com.example.android.apis/app/ReceiveResult.java</td>
            <td class="DescrColumn">Launches pick activity and receives its result</td>
        </tr>
        <tr>
            <td class="LinkColumn">src/com.example.android.apis/app/SendResult.java</td>
            <td class="DescrColumn">Allows user to pick an option and sends it back to its caller</td>
        </tr>
        <tr>
            <td class="LinkColumn">/res/any/layout/receive_result.xml</td>
            <td class="DescrColumn">Defines contents of the ReceiveResult screen</td>
        </tr>
        <tr>
            <td class="LinkColumn">/res/any/layout/send_result.xml</td>
            <td class="DescrColumn">Defines contents of the SendResult screen</td>
        </tr>
</table>

 */
object ReceiveResult {
  // Definition of the one requestCode we use for receiving resuls.
  final private val GET_CODE = 0
}

class ReceiveResult extends Activity {
  import ReceiveResult._  // companion object

  /**
   * Initialization of the Activity after it is first created.  Must at least
   * call {@link android.app.Activity#setContentView setContentView()} to
   * describe what is to be displayed in the screen.
   */
  override protected def onCreate(savedInstanceState: Bundle) {
    // Be sure to call the super class.
    super.onCreate(savedInstanceState)

    // See assets/res/any/layout/hello_world.xml for this
    // view layout definition, which is being set here as
    // the content of our screen.
    setContentView(R.layout.receive_result)

    // Retrieve the TextView widget that will display results.
    mResults = findViewById(R.id.results).asInstanceOf[TextView]

    // This allows us to later extend the text buffer.
    mResults.setText(mResults.getText, TextView.BufferType.EDITABLE)

    // Watch for button clicks.
    val getButton = findViewById(R.id.get).asInstanceOf[Button]
    getButton setOnClickListener mGetListener
  }

  /**
   * This method is called when the sending activity has finished, with the
   * result it supplied.
   * 
   * @param requestCode The original request code as given to
   *                    startActivity().
   * @param resultCode From sending activity as per setResult().
   * @param data From sending activity as per setResult().
   */
  override protected def onActivityResult(requestCode: Int, resultCode: Int,
		                          data: Intent) {
    // You can use the requestCode to select between multiple child
    // activities you may have started.  Here there is only one thing
    // we launch.
    if (requestCode == GET_CODE) {

      // We will be adding to our text.
      val text = mResults.getText.asInstanceOf[Editable]

      // This is a standard resultCode that is sent back if the
      // activity doesn't supply an explicit result.  It will also
      // be returned if the activity failed to launch.
      if (resultCode == RESULT_CANCELED) {
        text.append("(cancelled)")

        // Our protocol with the sending activity is that it will send
        // text in 'data' as its result.
      } else {
        text append "(okay "
        text append java.lang.Integer.toString(resultCode)
        text append ") "
        if (data != null) {
          text append data.getAction
        }
      }

      text append "\n"
    }
  }

  private val mGetListener = new OnClickListener {
    def onClick(v: View) {
      // Start the activity whose result we want to retrieve.  The
      // result will come back with request code GET_CODE.
      val intent = new Intent(ReceiveResult.this, classOf[SendResult])
      startActivityForResult(intent, GET_CODE)
    }
  }

  private var mResults: TextView = _
}

