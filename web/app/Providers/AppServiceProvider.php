<?php

namespace App\Providers;

use Schema;
use Illuminate\Support\Str;
use Illuminate\Support\ServiceProvider;
use Illuminate\Contracts\Validation\Factory as Validator;

class AppServiceProvider extends ServiceProvider
{

// use Braintree\Configuration as Braintree_Configuration;
    /**
     * Validator instance.
     *
     * @var \Illuminate\Contracts\Validation\Factory
     */
    protected $validator;

    /**
     * Bootstrap any application services.
     *
     * @param \Illuminate\Contracts\Validation\Factory $validator
     * @return void
     */
    public function boot(Validator $validator)
    {
        \Braintree_Configuration::environment(env('BTREE_ENVIRONMENT'));
        \Braintree_Configuration::merchantId(env('BTREE_MERCHANT_ID'));
        \Braintree_Configuration::publicKey(env('BTREE_PUBLIC_KEY'));
        \Braintree_Configuration::privateKey(env('BTREE_PRIVATE_KEY'));

        $this->validator = $validator;

        Schema::defaultStringLength(191);

        $this->loadCustomValidators();
    }

    /**
     * Register any application services.
     *
     * @return void
     */
    public function register()
    {
        if ($this->app->environment('local')) {
            $this->app->register(\Barryvdh\LaravelIdeHelper\IdeHelperServiceProvider::class);

            if (app_debug_enabled()) {
                $this->app->register(\Barryvdh\Debugbar\ServiceProvider::class);
            }
        }
    }

    /**
     * Load the custom validator methods.
     *
     * @return void
     */
    protected function loadCustomValidators()
    {
        $customValidatorClass = 'App\Base\Validators\CustomValidators';

        $this->extendValidator('mobile_number', $customValidatorClass);
        $this->extendValidator('numeric_max', $customValidatorClass);
        $this->extendValidator('numeric_min', $customValidatorClass);
        $this->extendValidator('otp', $customValidatorClass);
        $this->extendValidator('uuid', $customValidatorClass);
        $this->extendValidator('decimal', $customValidatorClass);
        $this->extendValidator('double', $customValidatorClass);
    }

    /**
     * Extend the validator with custom methods.
     *
     * @param string $name
     * @param string $class
     * @return void
     */
    protected function extendValidator($name, $class)
    {
        $method = 'validate' . Str::studly($name);

        $this->validator->extend($name, "{$class}@{$method}");
    }
}
