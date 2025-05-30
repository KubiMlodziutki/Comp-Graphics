import sys, math, pygame
from pygame.locals import DOUBLEBUF, OPENGL, QUIT
from OpenGL.GL import *
from OpenGL.GLU import *

BOARD = 8
SQ = 1
THICK = 0.5
WHITE_Z, BLACK_Z = 0, 7
RAD, CAM_H, LIGHT_H = 15, 10, 15
FPS = 60


def unit_cube():
    glBegin(GL_QUADS)
    for n, z in ((1, .5), (-1, -.5)):
        glNormal3f(0, 0, n)
        glVertex3f(-.5, -.5, z)
        glVertex3f(.5, -.5, z)
        glVertex3f(.5, .5, z)
        glVertex3f(-.5, .5, z)

    for n, x in ((1, .5), (-1, -.5)):
        glNormal3f(n, 0, 0)
        glVertex3f(x, -.5, -.5)
        glVertex3f(x, -.5, .5)
        glVertex3f(x, .5, .5)
        glVertex3f(x, .5, -.5)

    for n, y in ((1, .5), (-1, -.5)):
        glNormal3f(0, n, 0)
        glVertex3f(-.5, y, .5)
        glVertex3f(.5, y, .5)
        glVertex3f(.5, y, -.5)
        glVertex3f(-.5, y, -.5)
    glEnd()


def pawn(is_white, q):
    glColor3f(*((0.95, 0.95, 0.95) if is_white else (150 / 255, 75 / 255, 0)))

    glPushMatrix()
    glScalef(0.9, 0.3, 0.9)
    gluSphere(q,0.4,24,24)
    glPopMatrix()

    glPushMatrix()
    glScalef(0.4, 1.5, 0.4)
    glTranslatef(0, 0.2, 0)
    gluSphere(q, 0.4, 24, 24)
    glPopMatrix()

    glPushMatrix()
    glScalef(0.8, 0.8, 0.8)
    glTranslatef(0, 0.9, 0)
    gluSphere(q, 0.20, 24, 24)
    glPopMatrix()


def board_square(light):
    glColor3f(*((0.90, 0.90, 0.90) if light else (0.15, 0.15, 0.15)))
    glPushMatrix()
    glScalef(SQ, THICK, SQ)
    unit_cube()
    glPopMatrix()


def draw_board():
    half = BOARD * SQ / 2
    for x in range(BOARD):
        for z in range(BOARD):
            glPushMatrix()
            glTranslatef((x + .5 * SQ) * SQ - half, 0, (z + .5 * SQ) * SQ - half)
            board_square((x + z) & 1 == 0)
            glPopMatrix()


def draw_pawns(q):
    half = BOARD * SQ / 2
    for x in range(BOARD):
        for z, white in ((WHITE_Z, True), (BLACK_Z, False)):
            glPushMatrix()
            glTranslatef((x + .5 * SQ) * SQ - half, THICK - 0.2, (z + .5 * SQ) * SQ - half)
            pawn(white, q)
            glPopMatrix()


def init_gl(w, h):
    glEnable(GL_DEPTH_TEST)
    glEnable(GL_NORMALIZE)
    glEnable(GL_COLOR_MATERIAL)
    glColorMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE)

    glEnable(GL_LIGHTING)

    glEnable(GL_LIGHT0)
    glLightfv(GL_LIGHT0, GL_POSITION, [0, LIGHT_H, 0, 1])
    glLightfv(GL_LIGHT0, GL_SPOT_DIRECTION, [0, -1, 0])
    glLightf(GL_LIGHT0, GL_SPOT_CUTOFF, 50)
    glLightfv(GL_LIGHT0, GL_AMBIENT, [0.1] * 3 + [1])
    glLightfv(GL_LIGHT0, GL_DIFFUSE, [0.9] * 3 + [1])

    glEnable(GL_LIGHT1)
    glLightfv(GL_LIGHT1, GL_POSITION, [10, 8, 10, 1])
    glLightfv(GL_LIGHT1, GL_AMBIENT, [0.5, 0, 0, 1])
    glLightfv(GL_LIGHT1, GL_DIFFUSE, [0.7, 0.8, 0.6, 1])

    glMatrixMode(GL_PROJECTION)
    gluPerspective(45, w / float(h), 0.1, 50)
    glMatrixMode(GL_MODELVIEW)


def set_camera(angle):
    ex, ez = RAD * math.cos(angle), RAD * math.sin(angle)
    glLoadIdentity()
    gluLookAt(ex, CAM_H, ez, 0, 0, 0, 0, 1, 0)


def main():
    pygame.init()
    W, H = 1280, 720
    pygame.display.set_mode((W, H), DOUBLEBUF | OPENGL)
    init_gl(W, H)

    quad = gluNewQuadric()
    gluQuadricNormals(quad, GLU_SMOOTH)
    clk = pygame.time.Clock()
    angle = 0.0
    running = True

    while running:
        for e in pygame.event.get():
            if e.type == QUIT:
                running = False

        angle += 0.4 * math.pi / 180
        set_camera(angle)

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
        draw_board()
        draw_pawns(quad)

        pygame.display.flip()
        clk.tick(FPS)

    pygame.quit()
    sys.exit()


if __name__ == "__main__":
    main()