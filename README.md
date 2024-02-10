![Akropolis banner](https://user-images.githubusercontent.com/56933557/188349705-b1f1eb56-8e4b-42d2-b99d-f21552ec84c2.png)

BonesHub is a modern Minecraft hub server solution that is based on Akropolis by DevBlook which is based on DeluxeHub by ItsLewizz. (Very confusing)
It contains almost all of its features and configuration files are almost the same, so you can just
drop your configuration into the plugin's directory, make a few modifications and use it.

The main difference between BonesHub and Akropolis is that BonesHub uses more of the Bones Network plugins to it's benefit such as Clora to allow the use of the & color codes.

## How to

### Install

To use this plugin just a grab a binary from the [releases page](https://github.com/BoneDevInc/BonesHub/releases)
or [compile it](#compile) yourself and drop it into your `plugins/` directory. Take in mind that you will need to be
running Paper 1.20+ so BonesHub can run properly. We recommend using Purpur however as it is usually quicker. You can download Purpur from [here](https://purpurmc.org/downloads).

### Compile

Compiling BonesHub is pretty simple, just one command, and you're ready to go:

**Linux (and other UNIX derivatives):**

```bash
./gradlew shadowJar
```

**Windows:**

```batch
gradlew.bat shadowJar
```

Then you will find the binary under the `build/libs/` directory.

### Report bugs or request features

Reporting a bug or requesting a feature can be useful for further development of the plugin. To do that you just need
to fill one of the issue templates we made for you:
[Click here to report a bug](https://github.com/BoneDevInc/BonesHub/issues/new?assignees=ccsnova&labels=bug&template=bug_report.yml&title=A+brief+description+of+your+report)
or [click here to request a feature](https://github.com/BoneDevInc/BonesHub/issues/new?assignees=ccsnova&labels=enhancement&template=feature_request.yml&title=A+brief+description+of+your+request).

### Contribute

At the moment we don't have a lot of requirements to contribute, just make sure to clarify
the features or fixes that you introduce in your pull request and try to follow the
[Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) specification.

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for
details.
