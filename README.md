# Project 

## TODO

See [TODO](./doc/todo.md)


## Migration to kotlin 2 EAP (k2)

- **IDEA configuration**

  ![img.png](companion/docs/assets/idea_configuration.png)
  
  **Idea version:**

  IntelliJ IDEA 2024.1 RC (Ultimate Edition)
  Build #IU-241.14494.158, built on March 21, 2024
  Runtime version: 17.0.10+8-b1207.12 x86_64
  VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
  macOS 14.4.1
  Kotlin Plugin: K2 Kotlin Mode (Alpha)
  Kotlin: 241.14494.158-IJ

- **Maven kotlin related configuration**

```
    <java.version>17</java.version>
    <kotlin.version>2.0.0-Beta5</kotlin.version>
    <kotlin.compiler.languageVersion>2.0</kotlin.compiler.languageVersion>
    <kotlinx-coroutines.version>1.8.0</kotlinx-coroutines.version>
    <arrow.version>1.2.3</arrow.version>
    <kotest.version>5.8.1</kotest.version>
    <kotest-assertions-arrow-jvm.version>1.4.0</kotest-assertions-arrow-jvm.version>
```

## Further documentation

- https://kotlinlang.org/docs/configure-build-for-eap.html#adjust-versions-in-dependencies
- https://kotlinlang.org/docs/whatsnew-eap.html#how-to-update-to-kotlin-kotlineapversion
- https://kotlinlang.org/docs/whatsnew1920.html#enable-k2-in-maven