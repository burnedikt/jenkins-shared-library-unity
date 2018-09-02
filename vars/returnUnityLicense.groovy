#!/usr/bin/env groovy

def call(String unityVersion) {
  echo "Returning Unity Serial"
  powershell "u3d run -u ${unityVersion} -- -nographics -quit -batchmode -logFile editor.log -returnlicense"
}