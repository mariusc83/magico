<h1>Magico API<h1> 
=====================================

This is a simple API which uses the Annotations Processor to create event listeners methods for
your views inside an Activity or Fragment.

Pre-requisites
--------------

Android SDK v19

Getting Started
---------------

This sample uses the Gradle build system. To build this project, use the
"gradlew build" command or use "Import Project" in Android Studio.

Sample
-----------

1. First step is to define your methods which you want to use as handlers for the onClickEvent in your
Activity class with the @OnClickHandler Annotation.

<pre><code>
    @OnClickHandler(elementId = R.id.button1)
    public void onSubmit(View view) {
        Toast.makeText(this, R.string.submit_event_label, Toast.LENGTH_LONG).show();
    }

    @OnClickHandler(elementId = R.id.button2)
    public void onCancel(View view) {
        Toast.makeText(this, R.string.cancel_event_label, Toast.LENGTH_LONG).show();
    }
</code></pre>

2. Second step is to register your activity for being handled by Magico.

<pre><code>
    @Override
    protected void onResume() {
        super.onResume();
        Magico.registerForUIDelegate(this, findViewById(R.id.mainContainer));
    }
</code></pre>

3. Unregister when you no longer need to handle the events. Usually this should happen on the onPause().

<pre><code>
    @Override
    protected void onPause() {
        super.onPause();
        Magico.unregisterForUIDelegate(this, findViewById(R.id.mainContainer));
    }
</code></pre>

License
-------

Copyright 2014 The Android Open Source Project, Inc.

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.
