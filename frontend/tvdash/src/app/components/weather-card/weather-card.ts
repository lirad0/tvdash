import { Component, Input } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Component({
  selector: 'weather-card',
  templateUrl: './weather-card.html'
})

export class WeatherCard {
    @Input() url = '';

    constructor(private sanitizer: DomSanitizer) {}

    get safeUrl(): SafeResourceUrl {
        return this.sanitizer.bypassSecurityTrustResourceUrl(this.url);
    }
}