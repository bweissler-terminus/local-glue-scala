# local-glue-scala

Developing AWS Glue jobs __locally__ using Scala can be helpful when you want to benefit from the features of your
IDE, such as IntelliJ IDEA.  Luckily, AWS provides their Glue SDK via a repository that can be referenced via Maven
(and likely sbt, though I have not tried).

[This post](https://docs.aws.amazon.com/glue/latest/dg/aws-glue-programming-etl-libraries.html#develop-local-scala) 
is rather straightforward to follow.  Here, I will offer a few [additional tips for developing in IntelliJ](#tips).  And,
some [gotcha's](#gotchas) that tripped me up.

# Tips

## Scala Plugin
Be sure to have the Scala framework plugin installed.

![Scala plugin](docs/images/scala-for-intellij.png)

## Project Setup
Create your __maven project__, but then you need a couple additional steps.

Be sure to include the Scala SDK in your project (⌘,).

![Scala SDK](docs/images/scala-sdk-intellij.png)

Set up your modules properly.

![Scala modules](docs/images/scala-module-intellij.png)

## Run Configurations
Create a run configuration for the class you want to run.

__VM Options__
```
-Dspark.master=local[*] -Dspark.app.name=localrun
```

__Program Arguments__ (if desired)
```
--show=entourage
```

![Run configuration](docs/images/run-config-intellij.png)

## Logging
By default, the SDK logs a lot.  Do yourself a favor and add `log4j.properties` to `src/main/resources`, and optionally
use the GlueLogger -- an example is in this repo.

> This logging is only carried through to Glue in AWS if you upload the pass the `log4.properties` file to S3 and use the
> `--extra-files` special job parameter when configuing your Glue job.

# Gotchas
Make sure you are using the right Scala SDK version, as specified [here](https://docs.aws.amazon.com/glue/latest/dg/aws-glue-programming-etl-libraries.html#develop-local-scala).

Make sure you are using the right Java JDK version for your Scala version, as specified [here](https://docs.scala-lang.org/overviews/jdk-compatibility/overview.html).

If you get an error like `java.lang.NoSuchMethodError: scala.Predef$.` blah blah blah, this [stackoverflow answer](https://stackoverflow.com/a/46521546) 
clued me in that I had a conflicting Scala SDK in my Global Libraries configuration in IntelliJ.

I'm still trying to figure out how to use Glue's `getSource` method to connect to a locally running MySQL, but it's failing
to find the driver.  I'm hoping something [here](https://docs.aws.amazon.com/glue/latest/dg/connection-defining.html#connection-properties-jdbc)
might enlighten me.  _Watch this space._
