#pragma once
#include <string>
#include "Widget.h"

class Film 
{
protected:
	//film variables
	float pos_x;
	float pos_y;
	float depth;
	std::string title;
	int year;
	std::string dir;
	std::string actors;
	std::string genre;
	std::string summary;
	std::string summary1;
	std::string image;
	std::string subimage1;
	std::string subimage2;
	int subcount;
public:
	//getters
	std::string getTitle() { return title; }
	int getYear() { return year; }
	std::string getDirector() { return dir; }
	std::string getActors() { return actors; }
	std::string getGenre() { return genre; }
	std::string getSummary() { return summary; }
	std::string getSummary1() { return summary1; }
	std::string getImage() { return image; }
	std::string getSubImage1() { return subimage1; }
	std::string getSubImage2() { return subimage2; }
	int getSubCount() { return subcount; }
	
	//setters
	void setTitle(std::string t) { title = t; }
	void setYear(int y) { year = y; }
	void setDirector(std::string d) { dir = d; }
	void setActors(std::string a) { actors = a; }
	void setGenre(std::string g) { genre = g; }
	void setSummary(std::string s) { summary = s; }
	void setSummary1(std::string s) { summary1 = s; }
	void setImage(std::string i) { image = i; }
	void setSubImage1(std::string si1) { subimage1 = si1; }
	void setSubImage2(std::string si2) { subimage2 = si2; }
	void setSubCount(int sbc) { subcount = sbc; }

	//pos & depth methods
	float getPosX() { return pos_x; }
	float getPosY() { return pos_y; }
	void setPosX(float x) { pos_x = x; }
	void setPosY(float y) { pos_y = y; }
	void setDepth(float d) { depth = d; }
	float getDepth() { return depth; }
	bool isFront() { return (getPosX() >= 140.f && getPosX() <= 860.f && getDepth() == 1.0f); }
	bool isNotFront() { return (getPosX() >= 140.f && getPosX() <= 860.f && getDepth() == 0.0f); }
};

