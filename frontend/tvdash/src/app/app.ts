import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Tableau } from './components/tableau/tableau';
import { AppNav } from './components/nav/app-nav';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Tableau, AppNav],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {
  protected readonly title = signal('tvdash');
}
