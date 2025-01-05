#include "TextField.h"
#include "TextField.h"


void TextFieldTitle::update()
{
	graphics::MouseState ms;
	graphics::getMouseState(ms);

	float mx = graphics::windowToCanvasX(ms.cur_pos_x);
	float my = graphics::windowToCanvasY(ms.cur_pos_y);

	//update & write if textfield highlighted
	if (widget_highlighted)
	{
		searchtitle += write();
	}
}

void TextFieldTitle::draw()
{
	//draw only when dock is highlighted
	if (dock_highlighted)
	{
		//draw rect
		graphics::Brush br;
		float h = 1.0f * widget_highlighted;
		br.fill_color[0] = 0.0f;
		br.fill_color[1] = 0.0f;
		br.fill_color[2] = 0.0f;
		br.outline_opacity = 0.2f + 0.8f * h;
		graphics::drawRect(getPosX(), getPosY(), 220.f, 20.f, br);

		//draw text
		graphics::Brush br1;
		br1.fill_color[0] = 1.0f;
		br1.fill_color[1] = 1.0f;
		br1.fill_color[2] = 1.0f;
		graphics::drawText(getPosX() - 110.f, getPosY() + 5.f, 15.f, searchtitle, br1);
	}
}

void TextFieldTitle::init()
{
	//font
	graphics::setFont(std::string(ASSET_PATH) + "MossionDemo.otf");
}



void TextFieldActor::update()
{
	graphics::MouseState ms;
	graphics::getMouseState(ms);

	float mx = graphics::windowToCanvasX(ms.cur_pos_x);
	float my = graphics::windowToCanvasY(ms.cur_pos_y);

	//update & write if textfield highlighted
	if (widget_highlighted)
	{
		searchactors += write();
	}
}

void TextFieldActor::draw()
{
	//draw only when dock is highlighted
	if (dock_highlighted)
	{
		//draw rect
		graphics::Brush br;
		float h = 1.0f * widget_highlighted;
		br.fill_color[0] = 0.0f;
		br.fill_color[1] = 0.0f;
		br.fill_color[2] = 0.0f;
		br.outline_opacity = 0.2f + 0.8f * h;
		graphics::drawRect(getPosX(), getPosY(), 220, 20, br);

		//draw text
		graphics::Brush br1;
		br.fill_color[0] = 1.0f;
		br.fill_color[1] = 1.0f;
		br.fill_color[2] = 1.0f;
		graphics::drawText(getPosX() - 110.f, getPosY() + 5.f, 15.f, searchactors, br);
	}
}

void TextFieldActor::init()
{
	//font
	graphics::setFont(std::string(ASSET_PATH) + "MossionDemo.otf");
}

void TextFieldDirector::update()
{
	graphics::MouseState ms;
	graphics::getMouseState(ms);

	float mx = graphics::windowToCanvasX(ms.cur_pos_x);
	float my = graphics::windowToCanvasY(ms.cur_pos_y);

	//update & write if textfield highlighted
	if (widget_highlighted)
	{
		searchdirectors += write();
	}
}

void TextFieldDirector::draw()
{
	//draw only when dock is highlighted
	if (dock_highlighted)
	{
		//draw rect
		graphics::Brush br;
		float h = 1.0f * widget_highlighted;
		br.fill_color[0] = 0.0f;
		br.fill_color[1] = 0.0f;
		br.fill_color[2] = 0.0f;
		br.outline_opacity = 0.2f + 0.8f * h;
		graphics::drawRect(getPosX(), getPosY(), 220, 20, br);

		//draw text
		graphics::Brush br1;
		br1.fill_color[0] = 1.0f;
		br1.fill_color[1] = 1.0f;
		br1.fill_color[2] = 1.0f;
		graphics::drawText(getPosX() - 110.f, getPosY() + 5.f, 15.f, searchdirectors, br1);
	}
}

void TextFieldDirector::init()
{
	//font
	graphics::setFont(std::string(ASSET_PATH) + "MossionDemo.otf");
}

static float delay = 0.0f;
std::string TextField::write()
{
	writing = false;
	delay += graphics::getDeltaTime();
	char ascii = 0;
	bool caps = false;
	//caps if any of shift keys pressed
	if (graphics::getKeyState(graphics::SCANCODE_RSHIFT) || graphics::getKeyState(graphics::SCANCODE_LSHIFT))
	{
		caps = true;
	}

	//iterate keys & prints the key that is pressed in the keybord,uses a delay in order not to print one chracter more than one times
	for (int i = graphics::SCANCODE_A; i <= graphics::SCANCODE_Z; i++)
	{
		if (graphics::getKeyState((graphics::scancode_t)i))
		{
			if (delay < 200.f)
			{
				continue;
			}
			else
			{
				writing = true;
				delay = 0.f;
				if (caps)
				{
					ascii = (char)(i + 61);
				}
				else
				{
					ascii = (char)(i + 93);
				}
				std::string asc(1, ascii);
				return asc;
			}
		}
	}
	std::string empty = "";
	return empty;
}
