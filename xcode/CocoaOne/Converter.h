//
//  Converter.h
//  CocoaOne
//
//  Created by Daniel Lauzon on 09/06/08.
//  Copyright 2008 __MyCompanyName__. All rights reserved.
//

#import <Cocoa/Cocoa.h>


@interface Converter : NSObject {
    float sourceCurrencyAmount, rate;
    
}
@property(readwrite) float sourceCurrencyAmount, rate;

- (float)convertCurrency;

@end
