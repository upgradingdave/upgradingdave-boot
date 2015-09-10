A few custom [boot](http://boot-clj.com/) tasks

# boot-cider

Setting up boot to start a
[cider enabled repl](https://github.com/boot-clj/boot/wiki/Cider-REPL)
involves adding cider-nrepl as a dependency and also some
nrepl-middleware.

The `upgradingdave.boot-cider` task will attempt to automatically
discover which versions of `cider-nrepl` and `clj-refactor` elisp
packages are installed inside `.emacs.d/elpa` and it wires everything
up so that running the following will create a nice cider-enabled
nrepl

    boot cider nrepl

(make sure to call the cider task before the nrepl task)

You can also pass it specific version numbers for `cider-nrepl` and/or
`clj-refactor`

    boot cider --cider "0.9.1" --refactor "1.1.0" nrepl

# boot-expect

I wanted to use the expectations library with emacs and found [this
thread](https://github.com/jaycfields/expectations/issues/52).

`upgradingdave.boot-expect` is an attempt to write a boot task to run
expectations. It works pretty well, but definitely a work in progress.

To run expectations for all namespaces

    boot expect

You can specify namespaces

    boot expect -n my-ns.one -n my-ns.two

You can also filter on namespaces

    boot expect -f "(re-matches #\"my-ns.*\" %)"

