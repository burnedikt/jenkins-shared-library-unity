#!/usr/bin/env groovy

def install(unityVersion) {
  powershell """
  Write-Output 'Making sure u3d is installed.'
  if (Get-Command "u3d.exe" -ErrorAction SilentlyContinue)
  {
    gem update u3d
  }
  else
  {
    gem install u3d
  }
  Write-Output 'Trying to install Unity Version ${unityVersion}.'
  \$maximumRuntimeSeconds = 900
  \$process = Start-Process -FilePath u3d -ArgumentList 'install --trace --verbose ${unityVersion}' -PassThru -NoNewWindow
  try
  {
      \$process | Wait-Process -Timeout \$maximumRuntimeSeconds -ErrorAction Stop
      Write-Warning -Message 'Unity installation successfully finished within timeout.'
  }
  catch
  {
      Write-Warning -Message 'Unity installation exceeded timeout, will be killed now.'
      Taskkill /IM ruby.exe /F
      exit 1
  }
  """
}

def createProject(unityVersion, projectPath, unityEmail, unityPassword, unitySerial) {
  // Unity has some issues with batchmode so we need to open it once without batchmode before actually trying to start the license activation process
  // this might be fixed in a newer version, but for now we're stuck. see http://answers.unity.com/answers/1171215/view.html
  echo "Starting Unity once (non-headless) for license activation"
  powershell """
  \$maximumRuntimeSeconds = 30
  \$process = Start-Process -FilePath u3d -ArgumentList 'run -u ${unityVersion}' -PassThru -NoNewWindow
  try
  {
      \$process | Wait-Process -Timeout \$maximumRuntimeSeconds -ErrorAction Stop
      Write-Warning -Message 'Unity successfully completed within timeout.'
  }
  catch
  {
      Write-Warning -Message 'Unity exceeded timeout, will be killed now.'
      Taskkill /IM Unity.exe /F
  }
  """
  echo "Activating Unity license for ${unityEmail} & Creating new Unity Project"
  powershell "u3d run -u ${unityVersion} -- -nographics -quit -batchmode -logFile editor.log -createProject ${projectPath} -serial \"${unitySerial}\" -username \"${unityEmail}\" -password \"${unityPassword}\""
}

def returnSerial(unityVersion) {
  echo "Returning Unity Serial"
  powershell "u3d run -u ${unityVersion} -- -nographics -quit -batchmode -logFile editor.log -returnlicense"
}
