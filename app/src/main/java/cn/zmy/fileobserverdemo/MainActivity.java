package cn.zmy.fileobserverdemo;

import android.os.FileObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity
{
    private FileObserver fileObserver;
    private TextView textViewLog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.textViewLog = (TextView) findViewById(R.id.textViewLog);
        this.fileObserver = new FileObserver(getCacheDir().getAbsolutePath())
        {
            @Override
            public void onEvent(int event, String path)
            {
                final String log = "onEvent:" + Event.values()[(int) (Math.log(event)/Math.log(2))];
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        appendLog(log);
                    }
                });
            }
        };
        findViewById(R.id.buttonStart).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fileObserver.startWatching();
            }
        });
        findViewById(R.id.buttonStop).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fileObserver.stopWatching();
            }
        });
        findViewById(R.id.buttonCreate).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                File file = new File(getCacheDir(), "testFile");
                if (file.exists())
                {
                    appendLog("文件已存在");
                    return;
                }
                try
                {
                    file.createNewFile();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.buttonModify).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                File file = new File(getCacheDir(), "testFile");
                if (!file.exists())
                {
                    appendLog("文件不存在，请先创建文件");
                    return;
                }
                try
                {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(9);
                    fileOutputStream.close();
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        findViewById(R.id.buttonDelete).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                File file = new File(getCacheDir(), "testFile");
                if (!file.exists())
                {
                    appendLog("文件不存在，请先创建文件");
                    return;
                }
                file.delete();
            }
        });
    }

    private void appendLog(String log)
    {
        textViewLog.setText(textViewLog.getText() + "\n" + log);
    }

    static enum Event
    {
        ACCESS(0x00000001), MODIFY(0x00000002), ATTRIB(0x00000004),
        CLOSE_WRITE(0x00000008), CLOSE_NOWRITE(0x00000010), OPEN(0x00000020),
        MOVED_FROM(0x00000040), MOVED_TO(0x00000080), CREATE(0x00000100),
        DELETE(0x00000200), DELETE_SELF(0x00000400), MOVE_SELF(0x00000800);

        private int index;

        Event(int index)
        {
            this.index = index;
        }
    }
}
