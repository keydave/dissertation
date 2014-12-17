package com.kecso.game;

import com.kecso.socket.ServerSocketControl;
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.terrain.noise.ShaderUtils;
import com.jme3.terrain.noise.basis.FilteredBasis;
import com.jme3.terrain.noise.filter.IterativeFilter;
import com.jme3.terrain.noise.filter.OptimizedErode;
import com.jme3.terrain.noise.filter.PerturbFilter;
import com.jme3.terrain.noise.filter.SmoothFilter;
import com.jme3.terrain.noise.fractal.FractalSum;
import com.jme3.terrain.noise.modulator.NoiseModulator;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.ui.Picture;
import java.util.Calendar;
import com.kecso.game.planephysics.Aircrafts.DefaultAircraft;
import com.kecso.game.planephysics.IO.InputParameters;
import com.kecso.game.planephysics.IO.OutputParameters;
import com.kecso.game.planephysics.Physics.AircraftPhysicsModel;
import com.kecso.game.world.NoiseBasedWorld;
import com.kecso.game.world.World;
import jme3utilities.sky.SkyControl;
import com.kecso.socket.UdpMessage;

public class Simulator extends SimpleApplication {

    private static Thread socketThread;
    private BulletAppState bulletAppState;
    private World world;
    float maxWorldHeight = 200;
    int tileSize = 65;
    int blockSize = 129;
    private Node planeNode;
    private AircraftPhysicsModel aircraftPhysicsModel;
    private static ServerSocketControl serverSocketControl = new ServerSocketControl();
    private InputParameters input;
    private OutputParameters output;
    private double dtInSeconds = 0.05D;
    private long dtInMillis = (long) (dtInSeconds * 1000.0D);
    private long startTime;
    private long sleepTime;
    private SkyControl sc;
    private RigidBodyControl planePhy;
    private boolean update = true;
    private Node aileronLeft;
    private Node aileronRight;
    private Node elevatorLeft;
    private Node elevatorRight;
    private Node rudder;
    private Node rotor;
    private Node gearLeft;
    private Node gearRight;
    private double rudderAoa = 0.0D;
    private double oldRudderAoa = 0.0D;
    private double elevatorAoa = 0.0D;
    private double oldElevatorAoa = 0.0D;
    private double aileronAoa = 0.0D;
    private double oldAileronAoa = 0.0D;
    private double langindGearMin = 0.0D;
    private double landingGearMax = 1.2D;
    private double currentGearAngle = 0.0D;
    private AircraftPhysicsModel.Vector azimuth = new AircraftPhysicsModel.Vector();
    private AircraftPhysicsModel.Vector zenith = new AircraftPhysicsModel.Vector();
    private AircraftPhysicsModel.Vector norm = new AircraftPhysicsModel.Vector();
    private AudioNode audio1;
    private AudioNode audio2;
    private AudioNode audio3;
    private AudioNode audioBum;
    private ChaseCamera chaseCam;
    private CameraNode cameraNode;
    private BitmapText speedText;
    private BitmapText altText;
    private BitmapText rpmText;
    private BitmapText vertsText;

    public PhysicsSpace getPhysicsSpace() {
        return this.bulletAppState.getPhysicsSpace();
    }

    @Override
    public void simpleInitApp() {
        setDisplayFps(false);
        setDisplayStatView(false);
        socketThread = new Thread(serverSocketControl);
        socketThread.start();
        initializeSky();
        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(bulletAppState);

        createWorldWithNoise();

        this.stateManager.attach(world);

        if (settings.getRenderer().startsWith("LWJGL")) {
            DirectionalLightShadowRenderer bsr = new DirectionalLightShadowRenderer(assetManager, 512, 1);
            DirectionalLight directionalLight = new DirectionalLight();
            directionalLight.setDirection(new Vector3f(-0.5f, -0.3f, -0.3f).normalizeLocal());
            bsr.setLight(directionalLight);
            viewPort.addProcessor(bsr);
        }

        setupControlKeys();


        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));


        AmbientLight ambientLight = new AmbientLight();
        ambientLight.setName("ambient");
        rootNode.addLight(ambientLight);
        sc.getUpdater().setAmbientLight(ambientLight);


        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.5f, -1f, -0.3f).normalizeLocal());
        rootNode.addLight(dl);

        dl = new DirectionalLight();
        dl.setDirection(new Vector3f(0.5f, -0.1f, 0.3f).normalizeLocal());
        rootNode.addLight(dl);
        flyCam.setEnabled(false);
        createPlane();
        createHud();


    }

    private void createPlane() {
        planeNode = (Node) assetManager.loadModel("Models/plane/plane.j3o");
        planeNode.scale(0.5f);
        rootNode.attachChild(planeNode);

        planeNode.setShadowMode(RenderQueue.ShadowMode.Cast);
        planeNode.setLocalTranslation(new Vector3f(10, 200, 10));
        aircraftPhysicsModel = new AircraftPhysicsModel(new DefaultAircraft(), AircraftPhysicsModel.Difficulty.HARD);

        chaseCam = new ChaseCamera(cam, planeNode, inputManager);
        chaseCam.setSmoothMotion(true);
        chaseCam.setMinDistance(5);
        chaseCam.setMaxDistance(7);
        chaseCam.setChasingSensitivity(10);
        chaseCam.setDefaultHorizontalRotation(FastMath.PI);
        chaseCam.setDefaultVerticalRotation(0.2f);


        input = new InputParameters();
        input.setDt(this.dtInSeconds);



        planePhy = new RigidBodyControl(CollisionShapeFactory.createBoxShape(planeNode.getChild("main_body")));
        planePhy.setMass(1000f);
        planeNode.setName("craft");

        planeNode.addControl(planePhy);
        getPhysicsSpace().add(planePhy);
        planePhy.setKinematic(true);

        getPhysicsSpace().addCollisionListener(collisionListener);
        aileronLeft = (Node) planeNode.getChild("aileron_left");
        aileronRight = (Node) planeNode.getChild("aileron_right");
        elevatorLeft = (Node) planeNode.getChild("elevator_left");
        elevatorRight = (Node) planeNode.getChild("elevator_right");
        rudder = (Node) planeNode.getChild("rudder");
        rotor = (Node) planeNode.getChild("rotor");
        gearLeft = (Node) planeNode.getChild("wheel_left");
        gearRight = (Node) planeNode.getChild("wheel_right");

        audio1 = new AudioNode(assetManager, "Sounds/ME1.wav");
        audio2 = new AudioNode(assetManager, "Sounds/ME2.wav");
        audio3 = new AudioNode(assetManager, "Sounds/ME3.wav");

        audio1.setPositional(true);
        audio1.setLooping(true);
        audio1.setVolume(2);
        rootNode.attachChild(audio1);

        audio3.setPositional(true);
        audio3.setLooping(true);
        audio3.setVolume(2);
        rootNode.attachChild(audio3);

        audio2.setPositional(true);
        audio2.setLooping(true);
        audio2.setVolume(2);
        rootNode.attachChild(audio2);
        audioBum = new AudioNode(assetManager, "Sounds/BUM.wav");
        audioBum.setVolume(2);
        rootNode.attachChild(audioBum);
    }

    private void initializeSky() {
        sc = new SkyControl(assetManager, cam, 0.9f, true, true);
        rootNode.addControl(sc);
        sc.getSunAndStars().setHour(6f);
        sc.getSunAndStars().setObserverLatitude(37.4046f * FastMath.DEG_TO_RAD);
        sc.getSunAndStars().setSolarLongitude(Calendar.FEBRUARY, 10);
        sc.setCloudiness(1f);
        sc.getSunAndStars().setHour(12f);
        sc.setEnabled(true);
    }

    private Material createTerrainMaterial() {
        Material terrainMaterial = new Material(this.assetManager, "Common/MatDefs/Terrain/HeightBasedTerrain.j3md");

        float grassScale = 16;
        float dirtScale = 16;
        float rockScale = 16;

        // GRASS texture
        Texture grass = this.assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");

        grass.setWrap(WrapMode.Repeat);
        terrainMaterial.setTexture("region1ColorMap", grass);
        terrainMaterial.setVector3("region1", new Vector3f(88, 200, grassScale));

        // DIRT texture
        Texture dirt = this.assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg");
        dirt.setWrap(WrapMode.Repeat);
        terrainMaterial.setTexture("region2ColorMap", dirt);
        terrainMaterial.setVector3("region2", new Vector3f(0, 90, dirtScale));

        // ROCK texture

        Texture rock = this.assetManager.loadTexture("Textures/Terrain/Rock/Rock.PNG");
        rock.setWrap(WrapMode.Repeat);
        terrainMaterial.setTexture("region3ColorMap", rock);
        terrainMaterial.setVector3("region3", new Vector3f(198, 260, rockScale));

        terrainMaterial.setTexture("region4ColorMap", rock);
        terrainMaterial.setVector3("region4", new Vector3f(198, 260, rockScale));

        Texture rock2 = this.assetManager.loadTexture("Textures/Terrain/Rock2/rock.jpg");
        rock2.setWrap(WrapMode.Repeat);

        terrainMaterial.setTexture("slopeColorMap", rock2);
        terrainMaterial.setFloat("slopeTileFactor", 32);

        terrainMaterial.setFloat("terrainSize", blockSize);

        return terrainMaterial;
    }

    private void createWorldWithNoise() {
        NoiseBasedWorld newWorld = new NoiseBasedWorld(this, bulletAppState.getPhysicsSpace(), tileSize, blockSize);

        newWorld.setWorldHeight(192f);

        newWorld.setViewDistance(7);

        newWorld.setCacheTime(5000);

        Material terrainMaterial = createTerrainMaterial();
        newWorld.setMaterial(terrainMaterial);

        // create a noise generator
        FractalSum base = new FractalSum();

        base.setRoughness(0.7f);
        base.setFrequency(1.0f);
        base.setAmplitude(1.0f);
        base.setLacunarity(3.12f);
        base.setOctaves(8);
        base.setScale(0.02125f);
        base.addModulator(new NoiseModulator() {
            @Override
            public float value(float... in) {
                return ShaderUtils.clamp(in[0] * 0.5f + 0.5f, 0, 1);
            }
        });

        FilteredBasis ground = new FilteredBasis(base);

        PerturbFilter perturb = new PerturbFilter();
        perturb.setMagnitude(0.119f);

        OptimizedErode therm = new OptimizedErode();
        therm.setRadius(5);
        therm.setTalus(0.011f);

        SmoothFilter smooth = new SmoothFilter();
        smooth.setRadius(1);
        smooth.setEffect(0.7f);

        IterativeFilter iterate = new IterativeFilter();
        iterate.addPreFilter(perturb);
        iterate.addPostFilter(smooth);
        iterate.setFilter(therm);
        iterate.setIterations(1);

        ground.addPreFilter(iterate);

        newWorld.setFilteredBasis(ground);

        this.world = newWorld;
    }

    @Override
    public void simpleUpdate(float tpf) {

        this.startTime = System.currentTimeMillis();



        if (world.isLoaded() && update) {
            moveAircraft();
            this.sleepTime = (this.dtInMillis - (System.currentTimeMillis() - this.startTime));
            if (this.sleepTime > 0L) {
                try {
                    Thread.sleep(this.sleepTime / 3);
                } catch (InterruptedException localInterruptedException) {
                }
            }
        } else {
            manageSound(false, false);
        }


    }

    private void moveAircraft() {

        UdpMessage message = serverSocketControl.getUdpMessage();
        if (message != null) {
            com.kecso.socket.util.Quaternion q = new com.kecso.socket.util.Quaternion();
            q.setXYZW(message.getQuatX() != null ? message.getQuatX() : 0, message.getQuatY() != null ? message.getQuatY() : 0, message.getQuatZ() != null ? message.getQuatZ() : 0, message.getQuatW() != null ? message.getQuatW() : 0);
            double[] toEulerAngles = q.toEulerAngles();
            this.input.setPitchRate((float) (toEulerAngles[0] / Math.PI));
            this.input.setRollRate(-(float) (toEulerAngles[1] / Math.PI));
            this.input.setYawRate(message.isYaw() ? (-(float) (toEulerAngles[2] / Math.PI)) : null);
            this.input.setThrottleRate(message.getThrot() != null ? message.getThrot() : 0);
        } else {
            this.input.setPitchRate(null);
            this.input.setRollRate(null);
            this.input.setYawRate(null);
            this.input.setThrottleRate(null);
        }

        this.output = this.aircraftPhysicsModel.doComputations(this.input);

        planeNode.setLocalRotation(planeNode.getLocalRotation().mult(new Quaternion().fromAngleAxis((float) this.output.getPitchRate(), Vector3f.UNIT_Z)));
        planeNode.setLocalRotation(planeNode.getLocalRotation().mult(new Quaternion().fromAngleAxis(-(float) this.output.getRollRate(), Vector3f.UNIT_X)));
        planeNode.setLocalRotation(planeNode.getLocalRotation().mult(new Quaternion().fromAngleAxis(-(float) this.output.getYawRate(), Vector3f.UNIT_Y)));

        planeNode.move(planeNode.getLocalRotation().getRotationColumn(0).mult((float) output.getSpeedX() * 0.02f));
        planeNode.move(planeNode.getLocalRotation().getRotationColumn(2).mult((float) output.getSpeedY() * 0.02f));
        planeNode.move(planeNode.getLocalRotation().getRotationColumn(1).mult((float) output.getSpeedZ() * 0.02f));

        this.rudderAoa = (this.output.getRudderAoa() - this.oldRudderAoa);
        this.oldRudderAoa = this.output.getRudderAoa();
        this.elevatorAoa = (this.output.getElevatorAoa() - this.oldElevatorAoa);
        this.oldElevatorAoa = this.output.getElevatorAoa();
        this.aileronAoa = (this.output.getAileronAoa() - this.oldAileronAoa);
        this.oldAileronAoa = this.output.getAileronAoa();
        rotor.setLocalRotation(rotor.getLocalRotation().mult(new Quaternion().fromAngleAxis(-(float) (1.51D * this.output.getRpm() / this.output.getMaxEnginePower()), Vector3f.UNIT_X)));
        rudder.setLocalRotation(rudder.getLocalRotation().mult(new Quaternion().fromAngleAxis(2.0F * (float) Math.toRadians(this.rudderAoa), Vector3f.UNIT_Z)));
        elevatorRight.setLocalRotation(elevatorRight.getLocalRotation().mult(new Quaternion().fromAngleAxis(-2.0F * (float) Math.toRadians(this.elevatorAoa), Vector3f.UNIT_Y)));
        elevatorLeft.setLocalRotation(elevatorLeft.getLocalRotation().mult(new Quaternion().fromAngleAxis(-2.0F * (float) Math.toRadians(this.elevatorAoa), Vector3f.UNIT_Y)));
        aileronRight.setLocalRotation(aileronRight.getLocalRotation().mult(new Quaternion().fromAngleAxis((float) (1.8D * Math.toRadians(this.aileronAoa)), Vector3f.UNIT_Y)));
        aileronLeft.setLocalRotation(aileronLeft.getLocalRotation().mult(new Quaternion().fromAngleAxis(-(float) (1.8D * Math.toRadians(this.aileronAoa)), Vector3f.UNIT_Y)));

        double angle = 0.02;
        if (input.getGearDir() == InputParameters.GearDir.DOWN && currentGearAngle < landingGearMax) {

            currentGearAngle += angle;
            gearLeft.setLocalRotation(gearLeft.getLocalRotation().mult(new Quaternion().fromAngleAxis((float) angle, Vector3f.UNIT_X)));
            gearRight.setLocalRotation(gearRight.getLocalRotation().mult(new Quaternion().fromAngleAxis(-(float) angle, Vector3f.UNIT_X)));
        }
        if (input.getGearDir() == InputParameters.GearDir.UP && currentGearAngle > langindGearMin) {
            currentGearAngle -= angle;
            gearLeft.setLocalRotation(gearLeft.getLocalRotation().mult(new Quaternion().fromAngleAxis(-(float) angle, Vector3f.UNIT_X)));
            gearRight.setLocalRotation(gearRight.getLocalRotation().mult(new Quaternion().fromAngleAxis((float) angle, Vector3f.UNIT_X)));
        }

        Vector3f xNegate = planeNode.getLocalRotation().getRotationColumn(0).negate();
        Vector3f yNegate = planeNode.getLocalRotation().getRotationColumn(2);
        Vector3f zNegate = planeNode.getLocalRotation().getRotationColumn(1).negate();

        this.azimuth.x = xNegate.x;
        this.azimuth.y = xNegate.y;
        this.azimuth.z = xNegate.z;

        this.zenith.x = zNegate.x;
        this.zenith.y = zNegate.y;
        this.zenith.z = zNegate.z;

        this.norm.x = yNegate.x;
        this.norm.y = yNegate.y;
        this.norm.z = yNegate.z;

        this.aircraftPhysicsModel.normalizeGravity(this.azimuth, this.zenith, this.norm);
        serverSocketControl.setOutput(output);

        updateHud();

        manageSound(true, false);

    }

    private void manageSound(boolean playing, boolean crash) {

        if (crash) {
            audioBum.setLocalTranslation(planeNode.getLocalTranslation());
            audioBum.setLocalRotation(planeNode.getLocalRotation());
            audio1.stop();
            audio2.stop();
            audio3.stop();
            audioBum.playInstance();
            return;
        }

        if (!playing) {
            audio1.stop();
            audio2.stop();
            audio3.stop();
            audioBum.stop();
            return;
        }

        double soundValue = output.getRpm() / output.getMaxEnginePower();
        if (soundValue < 1f / 3f) {
            audio1.setLocalTranslation(planeNode.getLocalTranslation());
            audio1.setLocalRotation(planeNode.getLocalRotation());
            audio1.play();
            audio2.stop();
            audio3.stop();
        } else if (soundValue > 2f / 3f) {
            audio3.setLocalTranslation(planeNode.getLocalTranslation());
            audio3.setLocalRotation(planeNode.getLocalRotation());
            audio3.play();
            audio1.stop();
            audio2.stop();
        } else {
            audio2.setLocalTranslation(planeNode.getLocalTranslation());
            audio2.setLocalRotation(planeNode.getLocalRotation());
            audio2.play();
            audio1.stop();
            audio3.stop();
        }


    }

    @Override
    public void destroy() {
        super.destroy();
        socketThread.interrupt();
        if (world != null) {
            world.close();
        }
    }
    private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String binding, boolean value, float tpf) {
            switch (binding) {
                case "RollLeft":
                    input.setRoll_left(value);
                    break;
                case "RollRight":
                    input.setRoll_right(value);
                    break;
                case "PitchUp":
                    input.setPitch_up(value);
                    break;
                case "PitchDown":
                    input.setPitch_down(value);
                    break;
                case "YawLeft":
                    input.setYaw_left(value);
                    break;
                case "YawRight":
                    input.setYaw_right(value);
                    break;
                case "TrimUp":
                    input.setTrim_up(value);
                    break;
                case "TrimDown":
                    input.setTrim_down(value);
                    break;
                case "MoreThrottle":
                    input.setThrottle_inc(value);
                    break;
                case "LessThrottle":
                    input.setThrottle_dec(value);
                    break;
                case "GearUpDown":
                    if (value) {
                        input.setGearDir(input.getGearDir() == InputParameters.GearDir.UP ? InputParameters.GearDir.DOWN : InputParameters.GearDir.UP);
                    }
                    break;
            }

        }
    };

    private void setupControlKeys() {
        inputManager.addMapping("RollLeft", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("RollRight", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("PitchUp", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("PitchDown", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("YawLeft", new KeyTrigger(KeyInput.KEY_Z));
        inputManager.addMapping("YawRight", new KeyTrigger(KeyInput.KEY_X));
        inputManager.addMapping("MoreThrottle", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("LessThrottle", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("TrimUp", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("TrimDown", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("GearUpDown", new KeyTrigger(KeyInput.KEY_G));
        inputManager.addListener(actionListener, "RollLeft");
        inputManager.addListener(actionListener, "RollRight");
        inputManager.addListener(actionListener, "PitchUp");
        inputManager.addListener(actionListener, "PitchDown");
        inputManager.addListener(actionListener, "YawLeft");
        inputManager.addListener(actionListener, "YawRight");
        inputManager.addListener(actionListener, "MoreThrottle");
        inputManager.addListener(actionListener, "LessThrottle");
        inputManager.addListener(actionListener, "TrimUp");
        inputManager.addListener(actionListener, "TrimDown");
        inputManager.addListener(actionListener, "GearUpDown");
    }
    private PhysicsCollisionListener collisionListener = new PhysicsCollisionListener() {
        private boolean collided = false;

        @Override
        public void collision(PhysicsCollisionEvent event) {
            if (!collided && (event.getNodeA().getName().equals("craft") || event.getNodeB().getName().equals("craft"))) {

                ParticleEmitter fire = new ParticleEmitter("Emitter", Type.Triangle, 30);
                Material mat_red = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
                mat_red.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
                fire.setMaterial(mat_red);
                fire.setImagesX(2);
                fire.setImagesY(2); // 2x2 texture animation
                fire.setEndColor(new ColorRGBA(1f, 0f, 0f, 1f));   // red
                fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
                fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
                fire.setStartSize(1.5f);
                fire.setEndSize(0.1f);
                fire.setGravity(0, 0, 0);
                fire.setLowLife(10f);
                fire.setHighLife(15f);
                fire.getParticleInfluencer().setVelocityVariation(0.3f);
                chaseCam.setMinVerticalRotation(0.8f);

                planePhy.setKinematic(false);
                for (Spatial child : planeNode.getChildren()) {
                    Node childNode = (Node) child;
                    if (childNode.getName().equals("main_body") || childNode.getName().equals("cockpit") || childNode.getName().equals("glass_front")) {
                        continue;
                    }
                    planeNode.detachChild(childNode);
                    rootNode.attachChild(childNode);
                    childNode.setLocalTranslation(planeNode.getLocalTranslation());
                    RigidBodyControl phy = new RigidBodyControl(CollisionShapeFactory.createBoxShape(childNode));
                    phy.setMass(200f);
                    getPhysicsSpace().add(phy);
                    childNode.setShadowMode(RenderQueue.ShadowMode.Cast);
                    childNode.addControl(phy);
                    childNode.attachChild(fire);
                }
                manageSound(true, true);
                speedText.setText("0");
                vertsText.setText("0");
                rpmText.setText("0");

                collided = true;
                update = false;
            }
        }
    };

    private void createHud() {

        Picture pic = new Picture("HUD Picture");
        pic.setImage(assetManager, "Pictures/hud.png", true);
        pic.setWidth(settings.getWidth() / 2);
        pic.setHeight(settings.getHeight() / 10);
        pic.setPosition(settings.getWidth() / 2 - settings.getWidth() / 4, 0);
        guiNode.attachChild(pic);

        speedText = new BitmapText(guiFont, false);
        speedText.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        speedText.setColor(ColorRGBA.Black);                             // font color
        speedText.setText("0");             // the text
        speedText.setLocalTranslation(settings.getWidth() / 2 - settings.getWidth() / 6, settings.getHeight() / 16, 0); // position
        guiNode.attachChild(speedText);

        altText = new BitmapText(guiFont, false);
        altText.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        altText.setColor(ColorRGBA.Black);                             // font color
        altText.setText("0");             // the text
        altText.setLocalTranslation(settings.getWidth() / 2 - settings.getWidth() / 20, settings.getHeight() / 16, 0); // position
        guiNode.attachChild(altText);

        rpmText = new BitmapText(guiFont, false);
        rpmText.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        rpmText.setColor(ColorRGBA.Black);                             // font color
        rpmText.setText("0");             // the text
        rpmText.setLocalTranslation(settings.getWidth() / 2 + settings.getWidth() / 14, settings.getHeight() / 16, 0); // position
        guiNode.attachChild(rpmText);

        vertsText = new BitmapText(guiFont, false);
        vertsText.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        vertsText.setColor(ColorRGBA.Black);                             // font color
        vertsText.setText("0");             // the text
        vertsText.setLocalTranslation(settings.getWidth() / 2 + settings.getWidth() / 5.2f, settings.getHeight() / 16, 0); // position
        guiNode.attachChild(vertsText);
    }

    private void updateHud() {
        speedText.setText(String.format("%3.1f", output.getSpeed()));
        vertsText.setText(String.format("%3.1f", output.getVerticalSpeed()));
        altText.setText(String.format("%3.1f", output.getAltitude()));
        rpmText.setText(String.format("%3.1f", output.getRpm()));
    }
}
