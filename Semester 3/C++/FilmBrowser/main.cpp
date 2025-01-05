#include "graphics.h"
#include "Browser.h"
#include "defines.h"

//update browser
void update(float ms)
{
	Browser * app = reinterpret_cast<Browser *>(graphics::getUserData());
	app->update();
}

//draw browser
void draw()
{
	Browser * app = reinterpret_cast<Browser *>(graphics::getUserData());
	app->draw();
}

int main(int arg, char** argv)
{
	Browser myapp;

	graphics::createWindow(WINDOW_WIDTH, WINDOW_HEIGTH, "FilmBrowser");

	graphics::setUserData(&myapp);

	graphics::setDrawFunction(draw);
	graphics::setUpdateFunction(update);

	graphics::setCanvasSize(CANVAS_WIDTH, CANVAS_HEIGTH);
	graphics::setCanvasScaleMode(graphics::CANVAS_SCALE_FIT);

	myapp.init();

	graphics::startMessageLoop();

	return 0;
}