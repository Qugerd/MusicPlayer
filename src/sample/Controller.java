package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller {

    private String path;
    MediaPlayer mediaPlayer;
    Integer k = 1;
    int id = 0;
    int id2 = 0;
    ObservableList<Song> songList = FXCollections.observableArrayList();

    @FXML private ImageView imageView;
    @FXML private HBox layout;
    @FXML private JFXButton previousbtn;
    @FXML private JFXButton playbtn;
    @FXML private JFXButton nextbtn;
    @FXML private Label title_song;
    @FXML private Label artist;
    @FXML private Label duration;
    @FXML private JFXSlider progressSlider;
    @FXML private Label lrc;
    @FXML private StackPane method_tips;
    @FXML private JFXButton method;
    @FXML private JFXButton ChooseFile;
    @FXML private JFXSlider volumeSlider;
    @FXML private Label title;
    @FXML private TableView<Song> table;
    @FXML private TableColumn<Song, Integer> tbNumber;
    @FXML private TableColumn<Song, String> tbName;
    @FXML private TableColumn<Song, Double> tbDuration;

    public class Song{
        public Integer  count;
        public String name;
        public Double time;

        public Song(Integer count, String name, Double time) {

            this.count = count;
            this.name = name;
            this.time = time;
        }

        public Integer getCount() {
            return count;
        }

        public String getName() {
            return name;
        }

        public Double getTime() { return time; }

        public void setTime(Double time) {
            this.time = time;
        }
    }

    @FXML
    public void OpenFileMethod(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Audio Files", "*.mp3"));
        fileChooser.setInitialDirectory(new File("C:\\Users\\Imposter\\Music"));
        List<File> kek = fileChooser.showOpenMultipleDialog(null);


        table.setItems(songList);

        for(File file : kek)
        {
            MediaPlayer mediaPlayer2;
            Media mediaTime = new Media(file.toURI().toString());
            mediaPlayer2 = new MediaPlayer(mediaTime);

            mediaPlayer2.setOnReady(new Runnable() {
                @Override
                public void run() {
                    songList.add(new Song(k++, file.getName().replace(".mp3", ""),(double)Math.round(mediaPlayer2.getTotalDuration().toSeconds())));
                }
            });

        }

        Media media = new Media(kek.get(id).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, javafx.util.Duration oldValue, javafx.util.Duration newValue) {
            progressSlider.setValue(newValue.toSeconds());
            }});



        title_song.setText(kek.get(id).getName().replace(".mp3", ""));
        artist.setText("");

        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                Duration total = mediaPlayer.getTotalDuration();
                progressSlider.setMax(total.toSeconds());
            }
        });



        volumeSlider.setValue(10);
        mediaPlayer.setVolume(volumeSlider.getValue() / 100);
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mediaPlayer.setVolume(volumeSlider.getValue()/ 100);
            }
        });



        nextbtn.setOnAction(e -> {

            mediaPlayer.stop();
            if(id != kek.size() - 1)
            {
                table.getSelectionModel().selectNext();
                Media media_next = new Media(kek.get(++id).toURI().toString());
                mediaPlayer = new MediaPlayer(media_next);
                mediaPlayer.play();
                mediaPlayer.setVolume(volumeSlider.getValue() / 100);
                mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                    @Override
                    public void changed(ObservableValue<? extends Duration> observable, javafx.util.Duration oldValue, javafx.util.Duration newValue) {
                        progressSlider.setValue(newValue.toSeconds());
                    }});
                duration.textProperty().bind(Bindings.createStringBinding(() -> {
                            Duration time = mediaPlayer.getCurrentTime();
                            return String.format("%4d:%02d:%04.1f",
                                    (int) time.toHours(),
                                    (int) time.toMinutes() % 60,
                                    time.toSeconds() % 3600);
                        },
                        mediaPlayer.currentTimeProperty()));

                mediaPlayer.setOnReady(new Runnable() {
                    @Override
                    public void run() {
                        Duration total = mediaPlayer.getTotalDuration();
                        progressSlider.setMax(total.toSeconds());
                    }
                });
            }
            else{
                table.getSelectionModel().selectFirst();
                Media media_next = new Media(kek.get(id = 0).toURI().toString());
                mediaPlayer = new MediaPlayer(media_next);
                mediaPlayer.play();
                mediaPlayer.setVolume(volumeSlider.getValue() / 100);
                mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                    @Override
                    public void changed(ObservableValue<? extends Duration> observable, javafx.util.Duration oldValue, javafx.util.Duration newValue) {
                        progressSlider.setValue(newValue.toSeconds());
                    }});
                duration.textProperty().bind(Bindings.createStringBinding(() -> {
                            Duration time = mediaPlayer.getCurrentTime();
                            return String.format("%4d:%02d:%04.1f",
                                    (int) time.toHours(),
                                    (int) time.toMinutes() % 60,
                                    time.toSeconds() % 3600);
                        },
                        mediaPlayer.currentTimeProperty()));
                mediaPlayer.setOnReady(new Runnable() {
                    @Override
                    public void run() {
                        Duration total = mediaPlayer.getTotalDuration();
                        progressSlider.setMax(total.toSeconds());
                    }
                });
            }
            title_song.setText(kek.get(id).getName().replace(".mp3", ""));
        });

        previousbtn.setOnAction(e ->{

            mediaPlayer.stop();
           if(id != 0)
           {
               table.getSelectionModel().selectPrevious();
               Media media_next = new Media(kek.get(--id).toURI().toString());
               mediaPlayer = new MediaPlayer(media_next);
               mediaPlayer.play();
               mediaPlayer.setVolume(volumeSlider.getValue() / 100);
               mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                   @Override
                   public void changed(ObservableValue<? extends Duration> observable, javafx.util.Duration oldValue, javafx.util.Duration newValue) {
                       progressSlider.setValue(newValue.toSeconds());
                   }});
               duration.textProperty().bind(Bindings.createStringBinding(() -> {
                           Duration time = mediaPlayer.getCurrentTime();
                           return String.format("%4d:%02d:%04.1f",
                                   (int) time.toHours(),
                                   (int) time.toMinutes() % 60,
                                   time.toSeconds() % 3600);
                       },
                       mediaPlayer.currentTimeProperty()));
               mediaPlayer.setOnReady(new Runnable() {
                   @Override
                   public void run() {
                       Duration total = mediaPlayer.getTotalDuration();
                       progressSlider.setMax(total.toSeconds());
                   }
               });
           }
           else{
               table.getSelectionModel().selectLast();
               Media media_next = new Media(kek.get(id = kek.size() - 1).toURI().toString());
               mediaPlayer = new MediaPlayer(media_next);
               mediaPlayer.play();
               mediaPlayer.setVolume(volumeSlider.getValue() / 100);
               mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                   @Override
                   public void changed(ObservableValue<? extends Duration> observable, javafx.util.Duration oldValue, javafx.util.Duration newValue) {
                       progressSlider.setValue(newValue.toSeconds());
                   }});
               duration.textProperty().bind(Bindings.createStringBinding(() -> {

                           Duration time = mediaPlayer.getCurrentTime();
                           return String.format("%4d:%02d:%04.1f",
                                   (int) time.toHours(),
                                   (int) time.toMinutes() % 60,
                                   time.toSeconds() % 3600);
                       },
                       mediaPlayer.currentTimeProperty()));
               mediaPlayer.setOnReady(new Runnable() {
                   @Override
                   public void run() {
                       Duration total = mediaPlayer.getTotalDuration();
                       progressSlider.setMax(total.toSeconds());
                   }
               });
           }
            title_song.setText(kek.get(id).getName().replace(".mp3", ""));
        });

        duration.textProperty().bind(Bindings.createStringBinding(() -> {
                    Duration time = mediaPlayer.getCurrentTime();
                    return String.format("%4d:%02d:%04.1f",
                            (int) time.toHours(),
                            (int) time.toMinutes() % 60,
                            time.toSeconds() % 3600);
                },
                mediaPlayer.currentTimeProperty()));

        table.setOnMouseClicked(e->{
            if(e.getClickCount() == 2)
            {
                mediaPlayer.stop();
                Media media_next = new Media(kek.get(id = table.getSelectionModel().getFocusedIndex()).toURI().toString());
                mediaPlayer = new MediaPlayer(media_next);
                mediaPlayer.play();
                mediaPlayer.setVolume(volumeSlider.getValue() / 100);
                mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                    @Override
                    public void changed(ObservableValue<? extends Duration> observable, javafx.util.Duration oldValue, javafx.util.Duration newValue) {
                        progressSlider.setValue(newValue.toSeconds());
                    }});
                duration.textProperty().bind(Bindings.createStringBinding(() -> {
                            Duration time = mediaPlayer.getCurrentTime();
                            return String.format("%4d:%02d:%04.1f",
                                    (int) time.toHours(),
                                    (int) time.toMinutes() % 60,
                                    time.toSeconds() % 3600);
                        },
                        mediaPlayer.currentTimeProperty()));
                mediaPlayer.setOnReady(new Runnable() {
                    @Override
                    public void run() {
                        Duration total = mediaPlayer.getTotalDuration();
                        progressSlider.setMax(total.toSeconds());
                    }
                });
            }
            MediaPlayer.Status status = mediaPlayer.getStatus();

            if(status == MediaPlayer.Status.PLAYING)
            {
                mediaPlayer.pause();
                playbtn.setText("Play");
            }
            else
            {
                mediaPlayer.play();
                playbtn.setText("Pause");
            }
            title_song.setText(kek.get(id).getName().replace(".mp3", ""));
        });

    }

    @FXML
    public void initialize()
    {
        tbNumber.setCellValueFactory(new PropertyValueFactory<Song,Integer>("count"));
        tbName.setCellValueFactory(new PropertyValueFactory<Song,String>("name"));
        tbDuration.setCellValueFactory(new PropertyValueFactory<Song,Double>("time"));

    }

    @FXML
    public void progressClicked(MouseEvent mouseEvent) {
        mediaPlayer.seek(javafx.util.Duration.seconds(progressSlider.getValue()));
    }
    @FXML
    public void progressDragg(MouseEvent mouseEvent) {
        mediaPlayer.seek(javafx.util.Duration.seconds(progressSlider.getValue()));
    }

    @FXML
    void play(ActionEvent event) {

        if(table.getSelectionModel().getFocusedIndex() == 0)
        {
            table.getSelectionModel().selectFirst();
        }

        MediaPlayer.Status status = mediaPlayer.getStatus();

        if(status == MediaPlayer.Status.PLAYING)
        {
            mediaPlayer.pause();
            playbtn.setText("Play");
        }
        else
        {
            mediaPlayer.play();
            playbtn.setText("Pause");
        }
    }

    @FXML
    public void next(ActionEvent event) {

    }

    @FXML
    public void prev(ActionEvent event) {

    }
}
