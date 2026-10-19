# CHROMADDED-VOIP

Java-based VOIP server for real-time voice communication and 3D positional chat over UDP.

CHROMADDED-VOIP is an experimental voice communication server developed in Java, focused on real-time network communication and positional audio for interactive 3D environments.

The project is designed to work with a native client implementation and is currently under development.

Features and goals

- UDP networking — datagram-based communication for real-time applications.
- 3D positional voice chat — a project focused on spatial voice communication.
- Java implementation — server-side development on the Java platform.
- Native client integration — intended to work with the "CHROMADDED-VOIP-CLIENT-BASE" (https://github.com/Mauricio-MN/CHROMADDED-VOIP-CLIENT-BASE) project.

«Development status: Work in progress. The feature set and implementation details may change.»

Technology stack

Component| Technology
Server| Java
Network transport| UDP
Build system| Apache Maven
License| GNU AGPL-3.0

Getting started

Requirements

- Java Development Kit (JDK)
- Apache Maven

Clone the repository

git clone https://github.com/Mauricio-MN/CHROMADDED-VOIP.git
cd CHROMADDED-VOIP

Build

Compile and package the project using Maven:

mvn package

The generated artifacts will be placed in Maven's "target/" directory.

Note: The exact Java version, runtime configuration, and launch command depend on the project's current configuration.

Related project

"CHROMADDED-VOIP-CLIENT-BASE" (https://github.com/Mauricio-MN/CHROMADDED-VOIP-CLIENT-BASE)

The companion client-side project for the CHROMADDED-VOIP ecosystem.

Project status

This project is under active development. Protocol details, supported capabilities, and configuration options will be documented as the implementation evolves.

License

This project is licensed under the GNU Affero General Public License v3.0 (AGPL-3.0). See ""LICENSE"" (LICENSE) for the complete license text.

---

Developed as part of the CHROMADDED-VOIP project.
