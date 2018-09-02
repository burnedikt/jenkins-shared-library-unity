#!/usr/bin/env groovy

def call(String unityVersion) {
  echo "Trying to install Unity Version ${unityVersion}."
  powershell "gem install u3d"
  powershell "u3d install ${unityVersion}"
}