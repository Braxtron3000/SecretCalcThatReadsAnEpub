package com.finalproject.queerCalc;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.security.crypto.EncryptedFile;

import android.content.Intent;
import androidx.biometric.*;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;

public class SecondActivity extends AppCompatActivity {

    WebView webView;
    String baseUrl;
    Book book;
    EpubReader epubReader;
    int resourceNum = 3;
    Boolean showsTableOfContents = false;

    HashMap<String, Resource> titlesToResources = new HashMap<>();

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private androidx.biometric.BiometricPrompt.PromptInfo promptInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);




        //initializaton stuff
        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new MyWebViewClient());
        epubReader = new EpubReader();
        baseUrl = getResources().openRawResource(R.raw.equations).toString();
        book = null;
        try {
            book = epubReader.readEpub(getResources().openRawResource(R.raw.equations));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        webView.loadDataWithBaseURL(baseUrl, getResource(resourceNum), "text/html", "utf-8", null);

        ArrayList<String> chapters = getTableOfContents(book.getTableOfContents().getTocReferences(), 0);


        //set the recyclerview
        RecyclerView recyclerView = ((RecyclerView) findViewById(R.id.recyclerview));
        recyclerView.setAdapter(new ChapterAdapter(chapters));
        recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getApplicationContext(), recyclerView, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String title = ((TextView) view.findViewById(R.id.textView)).getText().toString();
                loadToWebView(fetchResourceByChapterTitle(title, book));
                recyclerView.setVisibility(View.GONE);
                showsTableOfContents = false;
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SecondActivity.this, MainActivity.class);
        setResult(RESULT_OK, intent);
        finish();
    }


    //TOOLBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.epubnavigation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.toTableOfContents) {

            //if it shows table of contents set the TOB visibility to gone & mark showsTableOfContents as false
            if (showsTableOfContents) {
                ((RecyclerView) findViewById(R.id.recyclerview)).setVisibility(View.GONE);
                showsTableOfContents = false;
            }
            //if it doesn't show the table of contents set the TOB visibility to visible & mark showsTableOfContents as true
            else {
                ((RecyclerView) findViewById(R.id.recyclerview)).setVisibility(View.VISIBLE);
                showsTableOfContents = true;
            }
            return true;
        } else if (item.getItemId() == R.id.nextChapter) {
            if (resourceNum + 1 != book.getSpine().getSpineReferences().size()) {
                resourceNum += 1;
                loadToWebView(getResource(resourceNum));
            } else Toast.makeText(this, "end of book", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.previousChapter) {
            if (resourceNum - 1 != 0) {
                resourceNum -= 1;
                loadToWebView(getResource(resourceNum));
            } else Toast.makeText(this, "this is the beginning!", Toast.LENGTH_SHORT).show();

            return true;
        } else {
            Toast.makeText(this, "ha ha ha u suck!", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }
    }


    //READING FILES
    private String getResource(int i) {
        String linez = "";
        StringBuilder sb = new StringBuilder();
        Resource resource = book.getSpine().getResource(i);

        try {
            InputStream is = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            try {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    //log.d(TAG, "getChapters: " + line);
                    linez = sb.append(line).append("\n").toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linez;
    }




    private ArrayList<String> getTableOfContents(List<TOCReference> tocReferences, int depth) {
        if (tocReferences == null) {
            return null;
        }
        ArrayList<String> chapters = new ArrayList<>();
        for (TOCReference tocReference : tocReferences) {
            chapters.add(tocReference.getTitle());
            //for each tocRerence add a title key and Resource value.
            titlesToResources.put(tocReference.getTitle(), tocReference.getResource());
            logTableOfContents(tocReference.getChildren(), depth + 1);
        }
        return chapters;
    }

    private String fetchResourceByChapterTitle(String chapterTitle, Book book) {
        String linez = "";
        Resource resource = titlesToResources.get(chapterTitle);
        resourceNum = book.getSpine().getResourceIndex(resource);
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            try {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    //log.d(TAG, "getChapters: " + line);
                    linez = sb.append(line).append("\n").toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linez;
    }

    //WEBVIEW
    private void loadToWebView(String presenter) {
        webView.loadDataWithBaseURL(baseUrl, presenter, "text/html", "utf-8", null);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Toast.makeText(webView.getContext(), "links are not allowed in this view", Toast.LENGTH_SHORT).show();
            return true;
        }


    }


    //LOGGING
    private void logTableOfContents(List<TOCReference> tocReferences, int depth) {
        if (tocReferences == null) {
            return;
        }
        for (TOCReference tocReference : tocReferences) {
            StringBuilder tocString = new StringBuilder();
            for (int i = 0; i < depth; i++) {
                tocString.append("\t");
            }
            tocString.append(tocReference.getTitle());
            Log.i("epublib", tocString.toString());

            logTableOfContents(tocReference.getChildren(), depth + 1);
        }
    }




}