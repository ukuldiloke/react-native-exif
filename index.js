import {
	Platform,
	NativeModules } from 'react-native';
import moment from 'moment';

var Exif = {}

function parseToJSObjectAndroid(exif){
	var output = {} 

	const intProps = [
		'WhiteBalance',
		'Flash',
		'ImageLength',
		'ImageWidth',
		'ISOSpeedRatings',
		'Orientation',
	];

	const floatProps = [
		'FNumber',
	];

	const dateProps = [
		'DateTime',
		'DateTimeDigitized',
	];

	for (var key in exif) {
		if (exif.hasOwnProperty(key)) {
			if (exif[key] !== null) {
				if (intProps.includes(key)) {
					output[key] = parseInt(exif[key]);
				}
				else if (floatProps.includes(key)) {
					output[key] = parseFloat(exif[key]);
				}
				else if (dateProps.includes(key)) {
					output[key] = moment(exif[key], 'YYYY:MM:DD HH:mm:ss').toDate();
				}
			}
			else {
				output[key] = exif[key];
			}
		}
	}

	return output
}

Exif.getExif = function (uri) {
	var path = uri.replace('file://', '')
	return NativeModules.ReactNativeExif.getExif(path).then(result => {
		if (Platform.OS === 'android') {
			return parseToJSObjectAndroid(result)
		} else {
			return result;
		}
	})
}


module.exports = Exif


/*ios

exifDic properties: {
	    ColorModel = RGB;
	    Depth = 8;
	    Orientation = 1;
	    PixelHeight = 240;
	    PixelWidth = 320;
	    ProfileName = "sRGB IEC61966-2.1";
	    "{Exif}" =     {
	        ColorSpace = 1;
	        PixelXDimension = 320;
	        PixelYDimension = 240;
	    };
	    "{JFIF}" =     {
	        DensityUnit = 0;
	        JFIFVersion =         (
	            1,
	            0,
	            1
	        );
	        XDensity = 72;
	        YDensity = 72;
	    };
	    "{TIFF}" =     {
	        Orientation = 1;
	    };
	}*/

/*
android
 {"WhiteBalance":"0","SubSecTime":null,"GPSDateStamp":null,"GPSLatitude":null,"GPSAltitude":null,"GPSAltitudeRef":null,"GPSLongitude":null,"SubSecTimeDigitized":null,"DateTime":"2016:06:18 21:07:03","GPSLongitudeRef":null,"FocalLength":"293/100","Flash":"0","ImageLength":"1836","DateTimeDigitized":"2016:06:18 21:07:03","GPSProcessingMethod":null,"ExposureTime":"0.030","Make":"SAMSUNG","FNumber":"2.400","GPSLatitudeRef":null,"GPSTimeStamp":null,"ImageWidth":"3264","SubSecTimeOriginal":null,"ISOSpeedRatings":"200","Model":"SM-G7105","Orientation":"6"}
*/
