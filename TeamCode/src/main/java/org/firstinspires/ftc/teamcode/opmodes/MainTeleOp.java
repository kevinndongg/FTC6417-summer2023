package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Hardware6417;


/*
Controls:

release everything: retracted state
a : release cone/intake
b : low height
x : medium height
y : high height
right trigger (hold) : turret to the right
left trigger (hold) : turret to the left
left joystick : strafe (field centric)
right joystick : turn
left joystick (press) : cone righting + snail drive

 */
@TeleOp(name = "Main TeleOp", group = "TeleOp")
public class MainTeleOp extends LinearOpMode {
    Hardware6417 robot;
    enum SLIDESTATE {
        zero,
        low,
        medium,
        high,
        coneRight,
        manual
    }
    enum TURRETSTATE {
        center,
        right,
        left,
    }
    enum WRISTSTATE {
        down,
        up,
        coneRight
    }
    enum TWISTERSTATE {
        right,
        left,
        center
    }
    enum ROBOTSTATE{
        intake,
        maneuvering,
        outtake,
        coneRight
    }

    SLIDESTATE slideState;
    TURRETSTATE turretState;
    WRISTSTATE wristState;
    TWISTERSTATE twisterState;
    ROBOTSTATE robotState;
    ROBOTSTATE lastRobotState;

    int numOfGamepads;
    @Override
    public void runOpMode() throws InterruptedException {
        Gamepad currentGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();
        Gamepad lastGamepad1 = new Gamepad();
        Gamepad lastGamepad2 = new Gamepad();
        robot = new Hardware6417(hardwareMap);
        initStates();
        initRobot();

        boolean grabbing = false;
        int dunk = 0;

        double slideZeroTime = Double.POSITIVE_INFINITY;
        double turnTurretTime = Double.POSITIVE_INFINITY;
        double grabTime = Double.POSITIVE_INFINITY;
        double timeSinceSlideZero, timeSinceTurretTurn, timeSinceGrab;
        double vertControl, horzControl, rotateControl;
        double driveMagnitude = 0;

        waitForStart();


        ElapsedTime totalTimer = new ElapsedTime();

        while(opModeIsActive()) {
            setNumOfGamepads();
            lastGamepad1.copy(currentGamepad1);
            lastGamepad2.copy(currentGamepad2);
            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);


            switch (robotState) {
                case intake:
                    slideState = SLIDESTATE.zero;
                    wristState = WRISTSTATE.down;
                    twisterState = TWISTERSTATE.center;
                    turretState = TURRETSTATE.center;
                    driveMagnitude = Constants.driveSpeedIntake;

                    // hold a
                    if(currentGamepad1.a) {
                        grabbing = false;
                    } else {
                        grabbing = true;
                        if(lastGamepad1.a) {
                            grabTime = totalTimer.seconds();
                        }
                        timeSinceGrab = totalTimer.seconds() - grabTime;
                        if(timeSinceGrab > Constants.wristGrabDelay) {
                            setRobotState(ROBOTSTATE.maneuvering);
                        }
                    }
                    break;
                case maneuvering:
                    slideState = SLIDESTATE.zero;
                    wristState = WRISTSTATE.up;
                    twisterState = TWISTERSTATE.center;
                    turretState = TURRETSTATE.center;
                    driveMagnitude = Constants.driveSpeedManeuvering;

                    // go to intake
                    if(currentGamepad1.a && robot.sliderIntakeReady()) {
                        setRobotState(ROBOTSTATE.intake);
                    }

                    // outtake slider presets
                    if(currentGamepad1.b && !lastGamepad1.b) {
                        slideState = SLIDESTATE.low;
                        setRobotState(ROBOTSTATE.outtake);
                    }
                    if(currentGamepad1.x && !lastGamepad1.x) {
                        slideState = SLIDESTATE.medium;
                        setRobotState(ROBOTSTATE.outtake);
                    }
                    if(currentGamepad1.y && !lastGamepad1.y) {
                        slideState = SLIDESTATE.high;
                        setRobotState(ROBOTSTATE.outtake);
                    }

                    // cone righting
                    if(currentGamepad1.options && !lastGamepad1.options) {
                        setRobotState(ROBOTSTATE.coneRight);
                        grabbing = false;
                    }
                    break;
                case outtake:
                    driveMagnitude = Constants.driveSpeedOuttake;
                    // TURNING TURRET
                    if(currentGamepad1.right_trigger > 0.1 && gamepad1.left_trigger > 0.1){
                        turretState = TURRETSTATE.center;
                    } else if(currentGamepad1.right_trigger > 0.1) {
                        turretState = TURRETSTATE.right;
                        if(!(lastGamepad1.right_trigger > 0.1)){
                            turnTurretTime = totalTimer.seconds();
                        }
                        timeSinceTurretTurn = totalTimer.seconds() - turnTurretTime;
                        if(timeSinceTurretTurn > Constants.wristTurretTurnDelay) {
                            wristState = WRISTSTATE.down;
                        }
                    } else if(currentGamepad1.left_trigger > 0.1) {
                        turretState = TURRETSTATE.left;
                        if(!(lastGamepad1.left_trigger > 0.1)) {
                            turnTurretTime = totalTimer.seconds();
                        }
                        timeSinceTurretTurn = totalTimer.seconds() - turnTurretTime;
                        if(timeSinceTurretTurn > Constants.wristTurretTurnDelay) {
                            wristState = WRISTSTATE.down;
                        }
                    } else {
                        turretState = TURRETSTATE.center;
                    }

                    // dunk slides
                    if(currentGamepad1.right_bumper) {
                        dunk = Constants.sliderDunkDelta;
                    } else {
                        dunk = 0;
                    }

                    // OTHER SLIDE PRESETS
                    if(currentGamepad1.b && !lastGamepad1.b) {
                        slideState = SLIDESTATE.low;
                    }
                    if(currentGamepad1.x && !lastGamepad1.x) {
                        slideState = SLIDESTATE.medium;
                    }
                    if(currentGamepad1.y && !lastGamepad1.y) {
                        slideState = SLIDESTATE.high;
                    }
                    if(currentGamepad1.a && !lastGamepad1.a) {
                        setRobotState(ROBOTSTATE.maneuvering);
                        slideZeroTime = totalTimer.seconds();
                    }

                    // toggle grabber
                    if(currentGamepad1.left_bumper && !lastGamepad1.left_bumper) {
                        grabbing = !grabbing;
                    }

                    // toggle wrist
                    if(currentGamepad1.dpad_up && !lastGamepad1.dpad_up) {
                        if(wristState == WRISTSTATE.up) {
                            wristState = WRISTSTATE.down;
                        } else {
                            wristState = WRISTSTATE.up;
                        }
                    }
                    break;
                case coneRight:
                    slideState = SLIDESTATE.coneRight;
                    wristState = WRISTSTATE.coneRight;
                    turretState = TURRETSTATE.center;
                    twisterState = TWISTERSTATE.center;
                    driveMagnitude = Constants.driveSpeedConeRight;

                    if(gamepad1.right_bumper && !lastGamepad1.right_bumper) {
                        setRobotState(ROBOTSTATE.maneuvering);
                    }

                    if(gamepad1.left_bumper && !lastGamepad1.left_bumper) {
                        grabbing = !grabbing;
                    }
                    break;
            }


            switch (slideState) {
                case zero:
                    timeSinceSlideZero = totalTimer.seconds() - slideZeroTime;
                    if(timeSinceSlideZero > Constants.slideStallDelay){
                        robot.resetSliders();
                    } else {
                        robot.autoSlide(0, Constants.slideBasePower);
                    }
                    break;
                case low:
                    robot.autoSlide(Constants.sliderLowPos - dunk, Constants.slideLowPower);
                    break;
                case medium:
                    robot.autoSlide(Constants.sliderMedPos - dunk, Constants.slideMedPower);
                    break;
                case high:
                    robot.autoSlide(Constants.sliderHighPos - dunk, Constants.slideHighPower);
                    break;
                case coneRight:
                    robot.autoSlide(Constants.sliderConeRightPos, Constants.slideBasePower);
                    break;
            }


            switch (wristState) {
                case up:
                    robot.autoWrist(Constants.wristUpPos);
                    break;
                case down:
                    robot.autoWrist(Constants.wristDownPos);
                    break;
                case coneRight:
                    robot.autoWrist(Constants.wristConeRightPos);
                    break;
            }

            switch(twisterState) {
                case center:
                    robot.autoTwister(Constants.twisterCenterPos);
                    break;
                case left:
                    robot.autoTwister(Constants.twisterLeftPos);
                    break;
                case right:
                    robot.autoTwister(Constants.twisterRightPos);
                    break;
            }

            switch(turretState) {
                case center:
                    robot.autoTurret(Constants.turretCenterPos);
                    break;
                case left:
                    robot.autoTurret(Constants.turretLeftPos);
                    break;
                case right:
                    robot.autoTurret(Constants.turretRightPos);
                    break;
            }

            if(grabbing) {
                robot.closeGrabber();
            } else {
                robot.openGrabber();
            }

            vertControl = Math.pow(-gamepad1.left_stick_y, 3);
            horzControl = Math.pow(gamepad1.left_stick_x, 3);
            rotateControl = Math.pow(gamepad1.right_stick_x, 3);

            telemetry.addData("robotState: ", robotState);
            telemetry.addData("slideState: ", slideState);
            telemetry.addData("turretState: ", turretState);
            telemetry.addData("wristState: ", wristState);
            telemetry.addData("twisterState: ", twisterState);
            telemetry.addData("driveMagnitude: ", driveMagnitude);
            telemetry.addData("dunk: ", dunk);
            robot.telemetry(telemetry);
            telemetry.update();
        }
    }

    public void setLastGamepads(Gamepad lastG1, Gamepad lastG2) {
        lastG1.copy(gamepad1);
        lastG2.copy(gamepad2);
    }

    public void setRobotState(ROBOTSTATE targetRobotState) {
        lastRobotState = robotState;
        robotState = targetRobotState;
    }

    public void setNumOfGamepads() {
        if(gamepad2.getGamepadId() == -1) {
            numOfGamepads = 1;
        } else {
            numOfGamepads = 2;
        }
    }

    public void initStates() {
        slideState = SLIDESTATE.zero;
        turretState = TURRETSTATE.center;
        wristState = WRISTSTATE.up;
        twisterState = TWISTERSTATE.center;
        robotState = ROBOTSTATE.maneuvering;
    }

    public void initRobot() {
        robot.retractOdo();
        robot.resetSliders();
        robot.autoWrist(Constants.wristUpPos);
        robot.openGrabber();
        robot.autoTurret(Constants.turretCenterPos);
        robot.autoTwister(Constants.twisterCenterPos);
    }
}
