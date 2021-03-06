# Spiner

<img src="https://raw.githubusercontent.com/mrombout/Spiner/develop/FrontEnd/src/main/resources/icon.png" alt="Spiner" title="Spiner" align="right" />

Spiner is a set of tools that allows you to easily author you own Choose Your Own Adventure books using [Twine 2](http://twinery.org/). This set consists of an application, which transforms your published Twine story into an EPUB ebook.

[![Build Status](https://travis-ci.org/mrombout/Spiner.svg?branch=develop)](https://travis-ci.org/mrombout/Spiner)

[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=spiner&metric=alert_status)](https://sonarcloud.io/dashboard?id=spiner)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=spiner&metric=sqale_index)](https://sonarcloud.io/dashboard?id=spiner)

## Features

The following features are currently supported:
<dl>
 <dt>Markdown</dt>
 <dd>Simple <a href="http://daringfireball.net/projects/markdown/">Markdown</a> syntax can be used to write and format your gamebook (EPUB only).</dd>
 <dt>Passage links</dt>
 <dd>Twine passage links (i.e. <code>[[displayed text|passage title]]</code>, <code>[[passage title]]</code>) are automatically converted to <code>&lt;a&gt;</code> links for EPUB.</dd>
 <dt>Story Stylesheet</dt>
 <dd>Your story stylesheet as defined in Twine will be add to every <code>.xhtml</code> file in your EPUB file.</dt>
 <dt>Images</dt>
 <dd>Both local (images on your harddrive) as remote images (images on the internet) can simply be embedded in the EPUB by using standard markdown syntax(e.g. <code>![alt](url)</code>).</dd>
 <dt>Metadata</dt>
 <dd>Any EPUB metadata can be set from inside a normal Twine passage named 'metadata' using XML.</dd>
</dl>

## Usage

### Graphical User Interface

By far the easiest way to get started is to let the GUI to do the work for you. Just download the correct version for your operating system and follow the instructions on the screen!

Operating System  | Download
----------------- | -------------
Windows           | _no download available yet_
Mac OS            | _no download available yet_
Linux             | _no download available yet_
Other             | _no download available yet_

### Commandline

There is also a separate command-line version that works like this:

```
usage: java -jar spiner-0.1.2.jar [-f <format>] [-help | -version] [-i <input>] [-o <file>]
 -f,--format <format>   output format, currently only (epub)
 -help                  display this help and exit
 -i,--file <input>      location of input HTML file
 -o,--output <file>     location of output file
 -version               output version information and exit
```

```
java -jar spiner-0.1.2.jar -f "epub" -i "My Cool Story.html" -o "My Cool Story.epub"
```

# Examples

| Book                | Link                                                                                                  |
| ------------------- | ----------------------------------------------------------------------------------------------------- |
| Kolb and the Dragon | [Twine](./example/Kolb%20and%20the%20Dragon.html) to [EPUB](./example/Kolb%20and%20the%20Dragon.epub) |
| Features            | [Twine](./example/Features.html) to [EPUB](./example/Features.epub)                                   |

## Roadmap

The following features are currently not implemented but are to be expected somewhere in the indefinite future, in roughly the following order:

<dl>
  <dt>Annotation tag</dt>
  <dd>Passages tagged with `annotation` will not be included in the EPUB file.</dd>
  <dt>Unified markup</dt>
  <dd>It would be nice to be able to transform the same Twine story into both an EPUB and LaTeX file without having to fix up tiny details in either of them.</dd>
  <dt>Scripting? and other EPUB3 features</dt>
  <dd>I currently do not own an eReader that supports EPUB3 features. But support for EPUB3 features, especially
  scripting could prove interesting.</dd>
</dl>

## License

See the file [`COPYING`](https://raw.githubusercontent.com/TwineSpiner/Spiner/master/COPYING).
