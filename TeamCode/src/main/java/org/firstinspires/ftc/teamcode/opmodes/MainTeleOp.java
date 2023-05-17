package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;


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
public class MainTeleOp extends LinearOpMode {
    enum SLIDESTATE {
        zero,
        low,
        medium,
        high,
        coneright,
        manual
    }

    enum TURRETSTATE {
        center,
        right,
        left,
    }

    enum ELBOWSTATE {
        down,
        up,
        coneRight
    }

    enum WRISTSTATE {
        counterClockwise,
        clockwise,
        straight
    }

    SLIDESTATE slideState;
    TURRETSTATE turretState;
    ELBOWSTATE elbowState;
    WRISTSTATE wristState;

    int numOfGamepads = 1;
    @Override
    public void runOpMode() throws InterruptedException {
        Gamepad lastGamepad1 = new Gamepad();
        Gamepad lastGamepad2 = new Gamepad();
        initStates();
        boolean grabbing = false;

        waitForStart();

        ElapsedTime totalTimer = new ElapsedTime();

        while(opModeIsActive()) {
            setNumOfGamepads();

            // slider control
            if(gamepad1.a && !lastGamepad1.a) {
                slideState = SLIDESTATE.zero;
            }
            if(gamepad1.b && !lastGamepad1.b) {
                slideState = SLIDESTATE.low;
            }
            if(gamepad1.x && !lastGamepad1.x) {
                slideState = SLIDESTATE.medium;
            }
            if(gamepad1.y && !lastGamepad1.y) {
                slideState = SLIDESTATE.high;
            }
            // cone righting
            if(gamepad1.left_stick_button && !lastGamepad1.left_stick_button) {
                slideState = SLIDESTATE.coneright;
                elbowState = ELBOWSTATE.coneRight;
            }
            // turret control
            if(gamepad1.left_trigger > 0.1) {
                turretState = TURRETSTATE.left;
            } else if(gamepad1.right_trigger > 0.1) {
                turretState = TURRETSTATE.right;
            } else {
                turretState = TURRETSTATE.center;
            }
            if(gamepad1.left_bumper && !lastGamepad1.left_bumper) {
                grabbing = !grabbing;
            }

            /*
            switch (slideState) {
                case zero:
                    robot.autoSlide(0);
                    break;
                case low:
                    robot.autoSldie(Constants.slideLowPos);
                    break;
                case medium:
                    robot.autoSlide(Constants.slideMedPos);
                    break;
                case high:
                    robot.autoSlide(Constants.slideHighPos);
                    break;
            }


             */
            /*switch (elbowState) {
                case up:
                    robot.autoElbow(Constants.elbowUpPos);
                    break;
                case down:
                    robot.autoElbow(Constants.elbowDownPos);
                    break;
                case coneRight:
                    robot.autoElbow(Constants.elbowConeRightpos);
                    break;
            }*/

            /*switch(wristState) {
                case straight:
                    robot.autoWrist(Constants.wristStraightPos);
                    break;
                case clockwise:
                    robot.autoWrist(Constants.wristClockwisePos);
                    break;
                case counterClockwise:
                    robot.autoWrist(Constants.wristCounterClockwisePos);
                    break;
            }*/

            /*switch(turretState) {
                case center:
                    robot.autoTurret(Constants.turretCenterPos);
                    break;
                case left:
                    robot.autoTurret(Constants.turretLeftPos);
                    break;
                case right:
                    robot.autoTurret(Constants.turretRightPos);
                    break;
            }*/

            setLastGamepads(lastGamepad1, lastGamepad2);
        }
    }

    public void setLastGamepads(Gamepad lastG1, Gamepad lastG2) {
        lastG1.copy(gamepad1);
        lastG2.copy(gamepad2);
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
        elbowState = ELBOWSTATE.up;
        wristState = WRISTSTATE.straight;
    }
}
