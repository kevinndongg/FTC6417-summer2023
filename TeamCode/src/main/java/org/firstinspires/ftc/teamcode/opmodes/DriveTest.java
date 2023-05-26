package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Hardware6417;
import org.firstinspires.ftc.teamcode.util.Encoder;

@TeleOp(name = "Drive Test", group = "TeleOp")
public class DriveTest extends LinearOpMode {
    Hardware6417 robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Hardware6417(hardwareMap);
        double angleOffset;
        double vertControl, horzControl, rotateControl;
        waitForStart();

        while (opModeIsActive()) {

            if(gamepad1.a) {
                angleOffset = robot.getRawExternalHeading();
            }

            Vector2d leftStickInput = new Vector2d(
                    gamepad1.left_stick_x,
                    gamepad1.left_stick_y)
                    .rotated(robot.getRawExternalHeading() - angleOffset
                    );

            telemetry.addData("rawExternalHeading", robot.getRawExternalHeading());
            telemetry.addData("externalHeading", robot.getExternalHeading());
            telemetry.update();
        }
    }
}
