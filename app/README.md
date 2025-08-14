# HelloJavaApp

A simple Android mobile app written in **Java** using **Android Studio**, **Android Gradle Plugin 8.2.2**, and **Gradle 8.5**.  
This is a starter template with a single activity (`MainActivity`) and a `ConstraintLayout` UI.

---

## 📱 Features
- Java-based Android app (no Kotlin)
- Uses Material Components
- Compatible with Android **minSdk 21** and **targetSdk 34**
- Ready for modern Gradle builds
- Includes a namespace (`com.example.hellojava`) for AGP 8.x compatibility

---

## 🛠 Requirements
- **Android Studio Giraffe** or newer
- **Java 17** (Recommended) or Java 21
- Gradle **8.5** (included via wrapper after sync)
- Android SDK 21+

---

## 📂 Project Structure
HelloJavaApp/ \n
├── app/ # Main application module
│ ├── build.gradle # Module-level Gradle config
│ ├── src/main/java/... # Java source files
│ ├── src/main/res/ # Resources (layouts, values, mipmap)
│ └── AndroidManifest.xml # App manifest
├── build.gradle # Project-level Gradle config
├── gradle.properties # Gradle settings
├── settings.gradle # Project settings
└── README.md # This file


---

## 🚀 How to Run
1. **Clone or extract** the project ZIP into a folder.
2. Open Android Studio → **File → Open...** → select the `HelloJavaApp` folder.
3. Wait for Gradle sync to finish (it will download dependencies).
4. Set **Gradle JDK** to **Java 17**:  
   *File → Settings → Build, Execution, Deployment → Build Tools → Gradle → Gradle JDK* → select **JDK 17**.
5. Click **Run ▶** to launch the app on an emulator or device.

---

## 🖼 Screenshot (example)
*(Add a screenshot here after running the app)*

---

## 📄 License
This project is provided as-is for learning and development purposes.  
You can freely modify and use it in your own projects.
