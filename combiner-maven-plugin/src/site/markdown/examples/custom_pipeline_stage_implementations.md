## Custom Pipeline Stage Implementations

This example assumes you have written a custom InputSourceReader named com.rss.reader.MyRssReader that pulls data from an RSS feed.  See the [developing custom pipeline stage tutorial](../developing_custom_pipeline_stage_tutorial.html) for details on how to write a custom pipeline implementation.

  This example is a little different because, instead of reading in files, the input resource is two RSS feeds that will be combined together.  So the ```inputResources``` defines the URLs to read.  The output source is a file, so the default FileOutputSourceWriter is used since no ```outputSourceWriter``` is defined.

  [Check maven central](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.jasonmfehr%22%20AND%20a%3A%22combiner-maven-plugin-api%22) for the latest version of the plugin.

```
<plugin>
  <groupId>com.github.jasonmfehr</groupId>
  <artifactId>combiner-maven-plugin</artifactId>
  <version>LATEST</version>
  <configuration>
    <combinations>
      <combination>
        <id>append-files</id>
        <inputSourceReader>com.rss.reader.MyRssReader</inputSourceReader>
        <inputResources>
          <includes>
            <include>http://some.rss.url.com/my-feed</include>
            <include>http://some.other.rss.url.com/the-other-feed</include>
          </includes>
        </inputResources>
        <combiner>AppendingCombiner</combiner>
        <outputDestination>rss-combined.txt</outputDestination>
      </combination>
    </combinations>
  </configuration>
</plugin>
```