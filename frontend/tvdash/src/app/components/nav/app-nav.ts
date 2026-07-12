import { Component, Inject, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { MediaQueryService } from '../../services/mq.service';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
	selector: 'app-nav',
	templateUrl: './app-nav.html',
	imports: [CommonModule, FormsModule, ButtonModule]
})
export class AppNav {
	#mediaService = inject(MediaQueryService);

	visible = false;
	name = '';
	url = '';
	imageDataUrl: string | null = null;
	imageFile: File | null = null;
	isMobile = toSignal(this.#mediaService.mediaQuery('max', 'md'));

	onFileChange(event: Event) {
		const input = event.target as HTMLInputElement;
		if (input.files && input.files[0]) {
			this.imageFile = input.files[0];
			const reader = new FileReader();
			reader.onload = () => {
				this.imageDataUrl = reader.result as string;
			};
			reader.readAsDataURL(this.imageFile);
		}
	}

	getSidebarTranslation(visible: boolean): string {
		let translation;

		if (this.isMobile()) {
			translation = visible ? 'translateX(0)' : 'translateX(-100%)';
		} else {
			translation = visible ? 'translateX(0)' : 'translateX(-768px)';
		}

		return translation;
	}

	save() {
		// implement saving behavior as needed; currently closes the sidebar
		this.visible = false;
	}

	open() { this.visible = true; }
	close() { this.visible = false; }
}
