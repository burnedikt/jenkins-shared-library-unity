# Shared Library for Jenkins Pipelines using Unity3D

Provides several helper steps when dealing with Unity3D in a Jenkins pipeline.

- `unity.install`: Installs the specified Unity Version on the system.
- `unity.createProject`: Creates a new Unity Project at the given location and takes
  care of activating a Unity serial at the same time.
- `unity.returnSerial`: Deactivates the existing Unity installation.

