import { Component, Inject, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { MediaQueryService } from '../../services/mq.service';
import { TableauService } from '../../services/tableau.service';
import { toSignal } from '@angular/core/rxjs-interop';
import { TableauCard } from '../../models/tableau-card';

@Component({
	selector: 'app-nav',
	templateUrl: './app-nav.html',
	styleUrls: ['./app-nav.css'],
	imports: [CommonModule, FormsModule, ButtonModule]
})
export class AppNav {
	#mediaService = inject(MediaQueryService);
	private readonly tableauService = inject(TableauService);

	visible = false;
	imageDataUrl: string | null = null;
	isMobile = toSignal(this.#mediaService.mediaQuery('max', 'md'));
	tableauCard: TableauCard = {
		id: '',
		name: '',
		imageUrl: null,
		file: null,
		url: ''
	};

	onFileChange(event: Event) {
		const input = event.target as HTMLInputElement;
		if (input.files && input.files[0]) {
			this.tableauCard.file = input.files[0];
			const reader = new FileReader();
			reader.onload = () => {
				this.imageDataUrl = reader.result as string;
			};
			reader.readAsDataURL(this.tableauCard.file);
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
		this.tableauService.saveCard({
			name: this.tableauCard.name,
			url: this.tableauCard.url,
			file: this.tableauCard.file,
			imageUrl: null,
			id: ''
		});
	}

	open() { this.visible = true; }
	close() { this.visible = false; }
}
