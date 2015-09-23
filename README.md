A few custom [boot](http://boot-clj.com/) tasks

# boot-cider

Setting up boot to start a
[cider enabled repl](https://github.com/boot-clj/boot/wiki/Cider-REPL)
involves adding cider-nrepl as a dependency and also some
nrepl-middleware.

The `upgradingdave.boot-cider` task will attempt to automatically
discover which versions of `cider-nrepl` and `clj-refactor` elisp
packages are installed inside `.emacs.d/elpa`. When it finds cider or
clj-refactor installed in elpa, this will wire everything up so that
running the following will create a nice cider-enabled nrepl:

    (require '[upgradingdave.boot-cider :refer [cider]])

    boot cider nrepl

(make sure to call the cider task before the nrepl task)

To make it even more automatic, you can customize cider to use this
cider task when doing `cider-jack-in`. Just add this to your emacs
config

    (setq cider-boot-parameters "cider repl -s wait")


You can also pass it specific version numbers for `cider-nrepl` and/or
`clj-refactor`

    boot cider --cider "0.9.1" --refactor "1.1.0" nrepl

# boot-expect

I wanted to use the expectations library with emacs and found [this
thread](https://github.com/jaycfields/expectations/issues/52).

`upgradingdave.boot-expect` is an attempt to write a boot task to run
expectations.

    (require '[upgradingdave.boot-expect :refer [expect]])

To run expectations for all namespaces

    boot expect

You can specify namespaces

    boot expect -n my-ns.one -n my-ns.two

You can also filter on namespaces

    boot expect -f "(re-matches #\"my-ns.*\" %)"

Run expectations whenever files change

    boot watch expect

Note: I encountered jvm out of memory exceptions when using Java 7. I
upgraded to Java 8 and haven't seen any problems since. 

# Themes for the speak task

As you know, a very important part of any build system is what sounds
it plays when the build succeeds or fails.

Add this as a dependency to your project and get some extra themes for
the `speak` task

	(merge-env!
	  [upgradingdave/boot-dave   "0.1.0"])

    (boot (speak :theme "homer"))

# install-clj

Task to create pom, create jar containing clj code, and deploy the jar
to local maven repository.

    (def +version+ "0.1.0")
	(task-options!
	 pom {:project 'my-group/my-project-name
	      :version +version+
	      :description "My awesome project"})

    (boot (install-clj))
