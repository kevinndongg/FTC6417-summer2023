package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.AprilTagDetectionPipeline;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Hardware6417;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.ArrayList;

@TeleOp(name="EOCVTest", group="Test")
public class EOCVTest extends LinearOpMode {
    Hardware6417 robot;
    ElapsedTime elapsedTime;
    OpenCvWebcam webcam;
    AprilTagDetectionPipeline pipeline;
    double fx;
    double fy;
    double cx;
    double cy;

    double tagsize;
    double tagX;
    double tagY;
    @Override
    public void runOpMode() throws InterruptedException {
        elapsedTime = new ElapsedTime();
        robot = new Hardware6417(hardwareMap);

        /*
         * Instantiate an OpenCvCamera object for the camera we'll be using.
         * In this sample, we're using a webcam. Note that you will need to
         * make sure you have added the webcam to your configuration file and
         * adjusted the name here to match what you named it in said config file.
         *
         * We pass it the view that we wish to use for camera monitor (on
         * the RC phone). If no camera monitor is desired, use the alternate
         * single-parameter constructor instead (commented out below)
         */
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam"); // put your camera's name here
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        pipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);
        webcam.setPipeline(pipeline);

        /*
         * Open the connection to the camera device. New in v1.4.0 is the ability
         * to open the camera asynchronously, and this is now the recommended way
         * to do it. The benefits of opening async include faster init time, and
         * better behavior when pressing stop during init (i.e. less of a chance
         * of tripping the stuck watchdog)
         *
         * If you really want to open synchronously, the old method is still available.
         */

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                /*
                 * Tell the webcam to start streaming images to us! Note that you must make sure
                 * the resolution you specify is supported by the camera. If it is not, an exception
                 * will be thrown.
                 *
                 * Keep in mind that the SDK's UVC driver (what OpenCvWebcam uses under the hood) only
                 * supports streaming from the webcam in the uncompressed YUV image format. This means
                 * that the maximum resolution you can stream at and still get up to 30FPS is 480p (640x480).
                 * Streaming at e.g. 720p will limit you to up to 10FPS and so on and so forth.
                 *
                 * Also, we specify the rotation that the webcam is used in. This is so that the image
                 * from the camera sensor can be rotated such that it is always displayed with the image upright.
                 * For a front facing camera, rotation is defined assuming the user is looking at the screen.
                 * For a rear facing camera or a webcam, rotation is defined assuming the camera is facing
                 * away from the user.
                 */
                webcam.startStreaming(640, 360, OpenCvCameraRotation.UPRIGHT);
                telemetry.addData("Stream", "started");
                telemetry.update();
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addData("Error", "Error code: " + errorCode);
            }
        });

        waitForStart();
        while(opModeIsActive()) {
            ArrayList<AprilTagDetection> detections = pipeline.getLatestDetections();

            int[] tagIds = new int[detections.size()];
            for(int i = 0; i < detections.size(); i++) {
                tagIds[i] = detections.get(i).id;
            }

            telemetry.addData("detections", detections.size());
            if(tagIds.length > 0) {
                telemetry.addData("tag id", tagIds[0]);
            }
            telemetry.addData("tagsize", tagsize);
            telemetry.addData("fx", fx);
            telemetry.addData("fy", fy);
            telemetry.addData("cx", cx);
            telemetry.addData("cy", cy);
            telemetry.addData("tagX", tagX);
            telemetry.addData("tagY", tagY);
            telemetry.update();

            sleep(200);
        }
    }
}
