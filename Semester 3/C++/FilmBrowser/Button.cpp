#include "Button.h"

void ClearFilterButton::update()
{
	graphics::MouseState ms;
	graphics::getMouseState(ms);

	float mx = graphics::windowToCanvasX(ms.cur_pos_x);
	float my = graphics::windowToCanvasY(ms.cur_pos_y);

	//plays sound if clicked
	if (ms.button_left_pressed && widget_highlighted)
	{
		graphics::playSound(std::string(ASSET_PATH) + "button.wav", 0.7f, 0);
	}
}

void ClearFilterButton::draw()
{
	//draw only when dock is highlighted
	if (dock_highlighted)
	{
		graphics::Brush br1;
		float h = 1.0f * widget_highlighted;
		br1.fill_opacity = 0.5f + 0.5f * h;
		br1.outline_opacity = 0.0f;
		br1.fill_color[0] = 255.0f;
		br1.fill_color[1] = 255.0f;
		br1.fill_color[2] = 0.0f;
		graphics::drawRect(getPosX(), getPosY(), 150, 25, br1);

		graphics::Brush br2;
		br2.fill_color[0] = 0.0f;
		br2.fill_color[1] = 0.0f;
		br2.fill_color[2] = 0.0f;
		graphics::drawText(getPosX() - 40, getPosY() + 5, 18.f, getLabel(), br2);
	}
}

void ClearFilterButton::init()
{
	//font
	graphics::setFont(std::string(ASSET_PATH) + "MossionDemo.otf");
}

void GenreButton::update()
{
	graphics::MouseState ms;
	graphics::getMouseState(ms);

	float mx = graphics::windowToCanvasX(ms.cur_pos_x);
	float my = graphics::windowToCanvasY(ms.cur_pos_y);

	//play music when clicked
	if (widget_highlighted && ms.button_left_pressed)
		graphics::playSound(std::string(ASSET_PATH) + "maximize.wav", 0.7f, 0);
}

void GenreButton::draw()
{
	//draw only when dock is highlighted
	if (dock_highlighted)
	{
		graphics::Brush br;
		br.fill_color[0] = 211.f;
		br.fill_color[1] = 211.f;
		br.fill_color[2] = 211.f;
		graphics::drawRect(getPosX(), getPosY(), 60.f, 12.f, br);

		graphics::Brush br1;
		br1.fill_color[0] = 0.0f;
		br1.fill_color[1] = 0.0f;
		br1.fill_color[2] = 0.0f;
		graphics::drawText(getPosX() - 19.f, getPosY() + 5.f, 13.f, getLabel(), br1);
	}
}

void GenreButton::init()
{
	//font
	graphics::setFont(std::string(ASSET_PATH) + "MossionDemo.otf");
}

void ArrowButton::update()
{
	graphics::MouseState ms;
	graphics::getMouseState(ms);

	float mx = graphics::windowToCanvasX(ms.cur_pos_x);
	float my = graphics::windowToCanvasY(ms.cur_pos_y);

	//true if mouse coordinates are in the subimage box
	if (contains(mx, my))
	{
		box_highlighted = true;
	}
	else
	{
		box_highlighted = false;
	}
}

void ArrowButton::draw()
{
	//if ^ draw arrow buttons
	if (box_highlighted)
	{
		graphics::Brush br;
		br.texture = std::string(ASSET_PATH) + "arrow.png";
		br.outline_opacity = 0.f;
		graphics::drawRect(getPosX1(), getPosY1(), 25, 25, br);
		graphics::setOrientation(-180.f);
		graphics::drawRect(getPosX2(), getPosY2(), 25, 25, br);
		graphics::resetPose();	
	}
}

void ArrowButton::init()
{
}

//true if x,y overlap arrow buttons
bool ArrowButton::containsor(float x, float y)
{
	return ((x >= 722.5f && x <= 747.5 && y >= 362.5f && y <= 387.5f) || (x >= 952.5f && x <= 977.5f && y >= 362.5f && y <= 387.5f));
}
